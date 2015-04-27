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
  protected LoadResult buildGeometry()
  {
    try
    {
      String fileName = String.format("Models/%s.raw", name);
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

      int trianglesCount = numBuffer.getInt(0);
      return new LoadResult(dataBuffer.asFloatBuffer(), trianglesCount);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
