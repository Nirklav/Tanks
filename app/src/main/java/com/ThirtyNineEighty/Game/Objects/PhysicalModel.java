package com.ThirtyNineEighty.Game.Objects;

import android.opengl.Matrix;
import android.util.Log;

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
  implements IPhysicalObject
{
  private float[] matrix;

  private int numOfTriangles;
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
  public ConvexHullResult getConvexHull(Vector3 planeNormal)
  {
    Vector<Vector2> projection = getDistinctProjection(planeNormal);
    Vector<Vector2> result = new Vector<Vector2>(projection.size());

    Vector2 first = getFirstPoint(projection);
    projection.remove(first);
    result.add(first);

    Collections.sort(projection, new Comparator<Vector2>()
    {
      @Override
      public int compare(Vector2 lhs, Vector2 rhs)
      {
        float angleLeft = lhs.getAngle(Vector2.xAxis);
        float angleRight = rhs.getAngle(Vector2.xAxis);

        if (angleLeft < angleRight)
          return -1;

        if (angleLeft > angleRight)
          return 1;

        return 0;
      }
    });

    first = projection.firstElement();
    projection.remove(first);
    result.add(first);

    for(Vector2 current : projection)
    {
      Vector2 firstPrevPoint = result.get(result.size() - 1);
      Vector2 secondPrevPoint = result.get(result.size() - 2);

      Vector2 prevVector = firstPrevPoint.subtract(secondPrevPoint);
      Vector2 currentVector = current.subtract(firstPrevPoint);

      float angle = prevVector.getAngle(currentVector);
      if (angle >= 180)
        result.remove(result.size() - 1);

      result.add(current);
    }

    //TODO: find normals
    return new ConvexHullResult(result, null);
  }

  private Vector2 getFirstPoint(Vector<Vector2> projection)
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

  private Vector<Vector2> getDistinctProjection(Vector3 planeNormal)
  {
    setGlobalVertices();
    planeNormal.normalize();

    Vector<Vector2> result = new Vector<Vector2>();

    for(Vector3 current : globalVertices)
    {
      Vector2 vector = getProjection(current, planeNormal);
      if (!result.contains(vector))
        result.add(vector);
    }

    return result;
  }

  private Vector2 getProjection(Vector3 global, Vector3 planeNormal)
  {
    Vector3 axisX = new Vector3(-planeNormal.getY(), planeNormal.getX(), 0);
    Vector3 axisY = axisX.getCross(planeNormal);

    float x = global.getX() * axisX.getX() + global.getY() * axisX.getY() + global.getZ() * axisX.getZ();
    float y = global.getX() * axisY.getX() + global.getY() * axisY.getY() + global.getZ() * axisY.getZ();

    return new Vector2(x, y);
  }

  @Override
  public void setGlobal(Vector3 position, float angleX, float angleY, float angleZ)
  {
    Matrix.setIdentityM(matrix, 0);
    Matrix.translateM(matrix, 0, position.getX(), position.getY(), position.getZ());
    Matrix.rotateM(matrix, 0, angleX, 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleY, 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleZ, 0.0f, 0.0f, 1.0f);
  }

  private void setGlobalVertices()
  {
    for (int i = 0; i < numOfTriangles; i++)
    {
      Vector3 local = vertices.get(i);
      Vector3 global = globalVertices.get(i);
      Matrix.multiplyMV(global.getRaw(), 0, matrix, 0, local.getRaw(), 0);
    }
  }

  @Override
  public Vector<Vector3> getGlobalNormals()
  {
    for(int i = 0; i < numOfTriangles; i++)
    {
      Vector3 local = normals.get(i);
      Vector3 global = globalNormals.get(i);
      Matrix.multiplyMV(global.getRaw(), 0, matrix, 0, local.getRaw(), 0);

      global.normalize();
    }

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

      numOfTriangles = dataBuffer.getInt(0);
      vertices = new Vector<Vector3>(numOfTriangles);
      normals = new Vector<Vector3>(numOfTriangles);

      globalVertices = createAndFill(numOfTriangles);
      globalNormals = createAndFill(numOfTriangles);

      for (int i = 0; i < numOfTriangles; i++)
      {
        float x = dataBuffer.getFloat();
        float y = dataBuffer.getFloat();
        float z = dataBuffer.getFloat();

        vertices.add(new Vector3(x, y, z));

        x = dataBuffer.getFloat();
        y = dataBuffer.getFloat();
        z = dataBuffer.getFloat();

        normals.add(new Vector3(x, y, z));

        // texture coord
        dataBuffer.getFloat();
        dataBuffer.getFloat();
      }

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
