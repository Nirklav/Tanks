package com.ThirtyNineEighty.Renderable.Resources;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileGeometrySource
  extends GeometrySource
{
  public FileGeometrySource(String name)
  {
    super(name, MeshMode.Static);
  }

  @Override
  public Geometry load()
  {
    LoadResult result = loadFromFile();
    return new Geometry(result.handle, result.numOfTriangles);
  }

  @Override
  public void reload(Geometry geometry)
  {
    release(geometry);

    LoadResult result = loadFromFile();
    geometry.updateData(result.handle, result.numOfTriangles);
  }

  private LoadResult loadFromFile()
  {
    try
    {
      String fileName = getModelFileName(name);
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
      int handle = loadGeometry(dataBuffer.asFloatBuffer());

      return new LoadResult(handle, numOfTriangles);
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
    public final int handle;
    public final int numOfTriangles;

    public LoadResult(int bufferHandle, int trianglesCount)
    {
      handle = bufferHandle;
      numOfTriangles = trianglesCount;
    }
  }
}
