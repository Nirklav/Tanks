package com.ThirtyNineEighty.Game.Objects;

import android.opengl.Matrix;
import android.util.Log;

import com.ThirtyNineEighty.Helpers.VectorUtils;
import com.ThirtyNineEighty.System.ActivityContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PhysicalModel
  implements IPhysicalObject
{
  private float[] matrix;

  private int numOfTriangles;
  private float[] vertices;
  private float[] normals;

  private float[] globalVertices;
  private float[] globalNormals;

  private float[] projection;

  public PhysicalModel(String fileName)
  {
    loadGeometry(fileName);
    matrix = new float[16];

    globalVertices = new float[numOfTriangles * 4];
    globalNormals = new float[numOfTriangles * 4];
  }

  @Override
  public ConvexHullResult getConvexHull(float[] planeNormal, int offset)
  {
    return null;
  }

  private int getFirstPointIndex(float[] vertices)
  {
    int minIndex = 0;

    for(int i = 0; i < vertices.length; i += 2)
      if (vertices[i] < vertices[minIndex])
        minIndex = i;

    return minIndex;
  }

  private float[] getProjection(float[] planeNormal, int offset)
  {
    if (projection == null)
      projection = new float[vertices.length];

    VectorUtils.normalize3(planeNormal, offset);

    for (int i = 0; i < projection.length; i++)
      getPointProjection(projection, i, globalVertices, i, planeNormal, offset);

    return projection;
  }

  private void getPointProjection(float[] result, int resultOffset, float[] vector, int vecOffset, float[] planeNormal, int norOffset)
  {
    float[] axisX = new float[] { -planeNormal[1], planeNormal[0], 0 };
    float[] axisY = new float[3];
    VectorUtils.getCross3(axisY, 0, planeNormal, norOffset, axisX, 0);

    result[0 + resultOffset] = vector[0 + vecOffset] * axisX[0] + vector[1 + vecOffset] * axisX[1] + vector[2 + vecOffset] * axisX[2];
    result[1 + resultOffset] = vector[0 + vecOffset] * axisY[0] + vector[1 + vecOffset] * axisY[1] + vector[2 + vecOffset] * axisY[2];
  }

  @Override
  public void setGlobal(float[] position, float angleX, float angleY, float angleZ)
  {
    Matrix.setIdentityM(matrix, 0);
    Matrix.translateM(matrix, 0, position[0], position[1], position[2]);
    Matrix.rotateM(matrix, 0, angleX, 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleY, 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(matrix, 0, angleZ, 0.0f, 0.0f, 1.0f);
  }

  public float[] getGlobalVertices()
  {
    for (int i = 0; i < numOfTriangles; i++)
    {
      int num = i * 4;
      Matrix.multiplyMV(globalVertices, num, matrix, 0, vertices, num);
    }

    return globalVertices;
  }

  @Override
  public float[] getGlobalNormals()
  {
    for(int i = 0; i < numOfTriangles; i++)
    {
      int num = i * 4;
      Matrix.multiplyMV(globalNormals, num, matrix, 0, normals, num);
      VectorUtils.normalize3(globalNormals, num);
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
      vertices = new float[numOfTriangles * 4];
      normals = new float[numOfTriangles * 4];

      for (int i = 0; i < numOfTriangles; i++)
      {
        int num = i * 4;

        vertices[num] = dataBuffer.getFloat();
        vertices[num + 1] = dataBuffer.getFloat();
        vertices[num + 2] = dataBuffer.getFloat();

        normals[num] = dataBuffer.getFloat();
        normals[num + 1] = dataBuffer.getFloat();
        normals[num + 2] = dataBuffer.getFloat();

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
}
