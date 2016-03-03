package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class FilePhGeometrySource
  implements ISource<PhGeometry>
{
  private String name;

  public FilePhGeometrySource(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public PhGeometry load()
  {
    try
    {
      InputStream stream = GameContext.context.getAssets().open(String.format("Models/%s.ph", name));

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size);
      dataBuffer.put(data, 0, size);
      dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
      dataBuffer.position(0);

      int numOfQuads = dataBuffer.getInt();
      ArrayList<Vector3> vertices = new ArrayList<>(numOfQuads * 4);
      ArrayList<Vector3> normals = new ArrayList<>();

      Vector3 position = Vector3.getInstance(dataBuffer);
      Vector3 angles = Vector3.getInstance(dataBuffer);

      for (int i = 0; i < numOfQuads * 4; i++)
      {
        Vector3 point = Vector3.getInstance(dataBuffer);
        if (!vertices.contains(point))
          vertices.add(point);

        Vector3 normal = Vector3.getInstance(dataBuffer);
        normal.normalize();

        boolean needAdd = true;
        for(Vector3 current : normals)
        {
          needAdd = !current.getCross(normal).isZero();
          if (!needAdd)
            break;
        }

        if (needAdd)
          normals.add(normal);
      }

      float radius = 0.0f;
      for(Vector3 current : vertices)
      {
        float currentLength = current.getLength();
        if (currentLength > radius)
          radius = currentLength;
      }

      dataBuffer.clear();
      return new PhGeometry(name, vertices, normals, position, angles, radius);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void reload(PhGeometry phGeometry) { }

  @Override
  public void release(PhGeometry phGeometry) { }
}
