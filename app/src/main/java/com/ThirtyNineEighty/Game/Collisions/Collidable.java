package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Collidable
  implements ICollidable
{
  private float[] matrix;

  private ArrayList<Vector3> vertices;
  private ArrayList<Vector3> normals;

  private ArrayList<Vector3> globalVertices;
  private ArrayList<Vector3> globalNormals;
  private boolean globalsInitialized;
  private Vector3 position;
  private Vector3 angles;

  private float radius;

  public Collidable(String fileName)
  {
    loadGeometry(String.format("Models/%s.ph", fileName));

    matrix = new float[16];

    position = Vector.getInstance(3);
    angles = Vector.getInstance(3);
    globalsInitialized = false;
  }

  @Override
  public ArrayList<Vector2> getConvexHull(Plane plane)
  {
    ArrayList<Vector2> projection = getDistinctProjection(plane);
    ArrayList<Vector2> convexHull = new ArrayList<Vector2>(projection.size());

    int firstIndex = getFirstPointIndex(projection);

    final Vector2 first = projection.remove(firstIndex);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    final Vector2 second = projection.remove(0);
    convexHull.add(second);

    Vector2 prevVector = Vector.getInstance(2);
    Vector2 currentVector = Vector.getInstance(2);

    for(Vector2 current : projection)
    {
      Vector2 firstPrevPoint = convexHull.get(convexHull.size() - 1);
      Vector2 secondPrevPoint = convexHull.get(convexHull.size() - 2);

      prevVector.setFrom(firstPrevPoint);
      prevVector.subtract(secondPrevPoint);

      currentVector.setFrom(current);
      currentVector.subtract(firstPrevPoint);

      float angle = prevVector.getAngle(currentVector);
      if (angle >= 180 && angle < 360)
        convexHull.remove(convexHull.size() - 1);

      convexHull.add(current);
    }

    Vector.release(prevVector);
    Vector.release(currentVector);

    return convexHull;
  }

  private static int getFirstPointIndex(ArrayList<Vector2> projection)
  {
    Vector2 minVector = null;
    int minVectorIndex = 0;

    int size = projection.size();
    for (int i = 0; i < size; i++)
    {
      Vector2 current = projection.get(i);

      if (minVector == null)
      {
        minVector = current;
        continue;
      }

      int compareX = Float.compare(current.getX(), minVector.getX());
      if (compareX < 0)
      {
        minVector = current;
        minVectorIndex = i;
      }

      if (compareX == 0)
      {
        int compareY = Float.compare(current.getY(), minVector.getY());
        if (compareY == 0)
          throw new IllegalArgumentException("projection has the same points");

        if (compareY > 0)
        {
          minVector = current;
          minVectorIndex = i;
        }
      }
    }

    return minVectorIndex;
  }

  private ArrayList<Vector2> getDistinctProjection(Plane plane)
  {
    Vector2 vector = Vector.getInstance(2);
    ArrayList<Vector2> result = new ArrayList<Vector2>();

    for(Vector3 current : globalVertices)
    {
      plane.getProjection(vector, current);
      if (!result.contains(vector))
        result.add(Vector.getInstance(2, vector));
    }

    Vector.release(vector);
    return result;
  }

  private static class AngleComparator implements Comparator<Vector2>
  {
    private Vector2 first;
    private Vector2 left;
    private Vector2 right;

    public AngleComparator(Vector2 first)
    {
      this.first = first;
      left = Vector.getInstance(2);
      right = Vector.getInstance(2);
    }

    @Override
    public int compare(Vector2 lhs, Vector2 rhs)
    {
      // sort angles counterclockwise

      // shift vectors to center
      left.setFrom(lhs);
      left.subtract(first);

      right.setFrom(rhs);
      right.subtract(first);

      // find angles
      float firstAngle = Vector2.xAxis.getAngle(left);
      float secondAngle = Vector2.xAxis.getAngle(right);

      // normalize angles for correct sorting
      // Example: 15, 45, 315, 345 => -45, -15, 15, 45
      if (firstAngle > 90)
        firstAngle -= 360;

      if (secondAngle > 90)
        secondAngle -= 360;

      // if angles equals compare by length
      if (Math.abs(firstAngle - secondAngle) <= Vector.epsilon)
      {
        float leftLength = left.getLength();
        float rightLength = right.getLength();

        // if angle > 0 sort by desc
        if (firstAngle >= 0)
          return Float.compare(rightLength, leftLength);

        return Float.compare(leftLength, rightLength);
      }

      // compare angles
      return Float.compare(firstAngle, secondAngle);
    }
  }

  @Override
  public void setGlobal(Vector3 pos, Vector3 ang)
  {
    if (globalsInitialized && pos.equals(position) && ang.equals(angles))
      return;

    globalsInitialized = true;
    position.setFrom(pos);
    angles.setFrom(ang);

    // vertices
    Matrix.setIdentityM(matrix, 0);
    Matrix.translateM(matrix, 0, position.getX(), position.getY(), position.getZ());
    Matrix.rotateM(matrix, 0, angles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angles.getZ(), 0.0f, 0.0f, 1.0f);

    int size = vertices.size();
    for (int i = 0; i < size; i++)
    {
      Vector3 local = vertices.get(i);
      Vector3 global = globalVertices.get(i);
      Matrix.multiplyMV(global.getRaw(), 0, matrix, 0, local.getRaw(), 0);
    }

    // normals
    Matrix.setIdentityM(matrix, 0);
    Matrix.rotateM(matrix, 0, angles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angles.getZ(), 0.0f, 0.0f, 1.0f);

    size = normals.size();
    for(int i = 0; i < size; i++)
    {
      Vector3 local = normals.get(i);
      Vector3 global = globalNormals.get(i);
      Matrix.multiplyMV(global.getRaw(), 0, matrix, 0, local.getRaw(), 0);

      global.normalize();
    }
  }

  @Override
  public ArrayList<Vector3> getGlobalVertices() { return globalVertices; }

  @Override
  public ArrayList<Vector3> getGlobalNormals() { return globalNormals; }

  @Override
  public float getRadius() { return radius; }

  private void loadGeometry(String fileName)
  {
    try
    {
      InputStream stream = GameContext.getAppContext().getAssets().open(fileName);

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size);
      dataBuffer.put(data, 0, size);
      dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
      dataBuffer.position(0);

      int numOfQuads = dataBuffer.getInt();
      vertices = new ArrayList<Vector3>(numOfQuads * 4);
      normals = new ArrayList<Vector3>();

      for (int i = 0; i < numOfQuads * 4; i++)
      {
        float x = dataBuffer.getFloat();
        float y = dataBuffer.getFloat();
        float z = dataBuffer.getFloat();

        Vector3 point = new Vector3(x, y, z);
        if (!vertices.contains(point))
          vertices.add(point);

        x = dataBuffer.getFloat();
        y = dataBuffer.getFloat();
        z = dataBuffer.getFloat();

        Vector3 normal = new Vector3(x, y, z);
        normal.normalize();

        boolean needAdd = true;
        for(Vector3 current : normals)
        {
          needAdd = !current.getCross(normal).equals(Vector3.zero);
          if (!needAdd)
            break;
        }

        if (needAdd)
          normals.add(normal);
      }

      radius = 0.0f;
      for(Vector3 current : vertices)
      {
        float currentLength = current.getLength();
        if (currentLength > radius)
          radius = currentLength;
      }

      globalVertices = createAndFill(vertices.size());
      globalNormals = createAndFill(normals.size());

      dataBuffer.clear();
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private ArrayList<Vector3> createAndFill(int count)
  {
    ArrayList<Vector3> result = new ArrayList<Vector3>(count);

    for(int i = 0; i < count; i++)
      result.add(new Vector3());

    return result;
  }
}
