package com.ThirtyNineEighty.Renderable.Resources;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FileGeometrySource
  extends GeometrySource
{
  private final String fileName;

  public FileGeometrySource(String name)
  {
    fileName = getModelFileName(name);
  }

  @Override
  public Geometry load()
  {
    LoadResult result = loadFromFile(fileName);
    int handle = loadGeometry(result.buffer);
    return new Geometry(handle, result.numOfTriangles);
  }

  @Override
  public void reload(Geometry geometry)
  {
    LoadResult result = loadFromFile(fileName);
    int handle = loadGeometry(result.buffer);
    geometry.updateData(handle, result.numOfTriangles);
  }

  private static LoadResult loadFromFile(String fileName)
  {
    try
    {
      InputStream stream = GameContext.getAppContext()
                                      .getAssets()
                                      .open(fileName);

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size - 4);
      dataBuffer.order(ByteOrder.nativeOrder());
      dataBuffer.put(data, 4, size - 4);
      dataBuffer.position(0);

      ByteBuffer numBuffer = ByteBuffer.allocateDirect(4);
      numBuffer.order(ByteOrder.nativeOrder());
      numBuffer.put(data, 0, 4);

      int numOfTriangles = numBuffer.getInt(0);
      FloatBuffer buffer = dataBuffer.asFloatBuffer();

      return new LoadResult(buffer, numOfTriangles);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private static String getModelFileName(String name)
  {
    return String.format("Models/%s.raw", name);
  }

  private static class LoadResult
  {
    public final FloatBuffer buffer;
    public final int numOfTriangles;

    public LoadResult(FloatBuffer buf, int triangles)
    {
      buffer = buf;
      numOfTriangles = triangles;
    }
  }
}
