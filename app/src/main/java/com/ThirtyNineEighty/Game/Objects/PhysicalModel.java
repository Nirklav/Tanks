package com.ThirtyNineEighty.Game.Objects;

import android.opengl.Matrix;
import android.util.Log;

import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.ActivityContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class PhysicalModel
  implements ICollidable
{
  private float[] matrix;

  private Vector<Vector3> vertices;
  private Vector<Vector3> normals;

  private Vector<Vector3> globalVertices;
  private Vector<Vector3> globalNormals;

  public PhysicalModel(String fileName)
  {
    loadGeometry(fileName);
    matrix = new float[16];
  }

  @Override
  public Vector<Vector2> getConvexHull(Plane plane)
  {
    Vector<Vector2> projection = getDistinctProjection(plane);
    Vector<Vector2> convexHull = new Vector<Vector2>();

    final Vector2 first = getFirstPoint(projection);
    projection.remove(first);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    final Vector2 second = projection.firstElement();
    projection.remove(second);
    convexHull.add(second);

    for(Vector2 current : projection)
    {
      Vector2 firstPrevPoint = convexHull.get(convexHull.size() - 1);
      Vector2 secondPrevPoint = convexHull.get(convexHull.size() - 2);

      Vector2 prevVector = firstPrevPoint.getSubtract(secondPrevPoint);
      Vector2 currentVector = current.getSubtract(firstPrevPoint);

      float angle = prevVector.getAngle(currentVector);
      if (angle >= 180 && angle < 360)
        convexHull.remove(convexHull.size() - 1);

      convexHull.add(current);
    }

    return convexHull;
  }

  private static Vector2 getFirstPoint(Vector<Vector2> projection)
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

  private Vector<Vector2> getDistinctProjection(Plane plane)
  {
    Vector<Vector2> result = new Vector<Vector2>();
    Vector2 vector = new Vector2();

    for(Vector3 current : getGlobalVertices())
    {
      plane.getProjection(vector, current);
      if (!result.contains(vector))
        result.add(new Vector2(vector));
    }

    return result;
  }

  private static class AngleComparator implements Comparator<Vector2>
  {
    private Vector2 first;
    private Vector2 lhsVector = new Vector2();
    private Vector2 rhsVector = new Vector2();

    public AngleComparator(Vector2 first)
    {
      this.first = first;
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
  public void setGlobal(Vector3 position, float angleX, float angleY, float angleZ)
  {
    // vertices
    Matrix.setIdentityM(matrix, 0);
    Matrix.translateM(matrix, 0, position.getX(), position.getY(), position.getZ());
    Matrix.rotateM(matrix, 0, angleX, 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleY, 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleZ, 0.0f, 0.0f, 1.0f);

    int size = vertices.size();
    for (int i = 0; i < size; i++)
    {
      Vector3 local = vertices.get(i);
      Vector3 global = globalVertices.get(i);
      Matrix.multiplyMV(global.getRaw(), 0, matrix, 0, local.getRaw(), 0);
    }

    // normals
    Matrix.setIdentityM(matrix, 0);
    Matrix.rotateM(matrix, 0, angleX, 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleY, 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleZ, 0.0f, 0.0f, 1.0f);

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
  public Vector<Vector3> getGlobalVertices()
  {
    return globalVertices;
  }

  @Override
  public Vector<Vector3> getGlobalNormals()
  {
    return globalNormals;
  }

  private void loadGeometry(String fileName)
  {
    try
    {
      InputStream stream = ActivityContext.getContext().getAssets().open(fileName);

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size);
      dataBuffer.put(data, 0, size);
      dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
      dataBuffer.position(0);

      int numOfTriangles = dataBuffer.getInt();
      vertices = new Vector<Vector3>(numOfTriangles);
      normals = new Vector<Vector3>(numOfTriangles);

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
          needAdd = !current.equals(normal);
          needAdd &= !current.getCross(normal).equals(Vector3.zero);

          if (!needAdd)
            break;
        }

        if (needAdd)
          normals.add(normal);
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

  private Vector<Vector3> createAndFill(int count)
  {
    Vector<Vector3> result = new Vector<Vector3>(count);

    for(int i = 0; i < count; i++)
      result.add(new Vector3());

    return result;
  }
}
