package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileGeometrySource
  extends GeometrySource
{
  // File format:
  // Header:
  // [int] - triangles Count
  // [float, float, float] - local position
  // [float, float, float] - local angles in degrees
  // Data:
  // foreach pack (triangles Count * 3)
  // {
  //   [float, float, float] - vertex 1
  //   [float, float, float] - normal 1
  //   [float, float] - texture coords
  // }

  private final static int headerSize = 4 + 12 + 12;

  private String fileName;

  public FileGeometrySource(String name)
  {
    super(name, MeshMode.Static);

    fileName = name;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    try
    {
      InputStream stream = GameContext.context.getAssets().open(String.format("Models/%s.raw", fileName));

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size - headerSize);
      dataBuffer.order(ByteOrder.nativeOrder());
      dataBuffer.put(data, headerSize, size - headerSize);
      dataBuffer.position(0);

      ByteBuffer headerBuffer = ByteBuffer.allocateDirect(headerSize);
      headerBuffer.order(ByteOrder.nativeOrder());
      headerBuffer.put(data, 0, headerSize);
      headerBuffer.position(0);

      int trianglesCount = headerBuffer.getInt();
      Vector3 position = Vector3.getInstance(headerBuffer);
      Vector3 angles = Vector3.getInstance(headerBuffer);

      return new LoadResult(dataBuffer.asFloatBuffer(), trianglesCount * 3, position, angles);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
