package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;
import android.util.Log;

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

  private float radius;

  public Collidable(String fileName)
  {
    loadGeometry(String.format("Models/%s.ph", fileName));
    matrix = new float[16];
  }

  @Override
  public ArrayList<Vector2> getConvexHull(Plane plane)
  {
    ArrayList<Vector2> projection = getDistinctProjection(plane);
    ArrayList<Vector2> convexHull = new ArrayList<Vector2>();

    final Vector2 first = getFirstPoint(projection);
    projection.remove(first);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    final Vector2 second = projection.get(0);
    projection.remove(second);
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

  private static Vector2 getFirstPoint(ArrayList<Vector2> projection)
  {
    Vector2 minVector = null;

    for(Vector2 current : projection)
    {
      if (minVector == null)
      {
        minVector = current;
        continue;
      }

      if (current.getX() < minVector.getX())
        minVector = current;
    }

    return minVector;
  }

  private ArrayList<Vector2> getDistinctProjection(Plane plane)
  {
    ArrayList<Vector2> result = new ArrayList<Vector2>();

    Vector2 vector = Vector.getInstance(2);

    for(Vector3 current : getGlobalVertices())
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
    private Vector2 lhsVector;
    private Vector2 rhsVector;

    public AngleComparator(Vector2 first)
    {
      this.first = first;
      lhsVector = new Vector2();
      rhsVector = new Vector2();
    }

    @Override
    public int compare(Vector2 lhs, Vector2 rhs)
    {
      lhsVector.setFrom(first);
      lhsVector.subtract(lhs);

      rhsVector.setFrom(first);
      rhsVector.subtract(rhs);

      float angle = lhsVector.getAngle(rhsVector);
      return angle > 180f ? 1 : -1;
    }
  }

  @Override
  public void setGlobal(Vector3 position, Vector3 angles)
  {
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

      int numOfTriangles = dataBuffer.getInt();
      vertices = new ArrayList<Vector3>(numOfTriangles);
      normals = new ArrayList<Vector3>(numOfTriangles);

      for (int i = 0; i < numOfTriangles * 3; i++)
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
      Log.e("Error", e.getMessage());
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
