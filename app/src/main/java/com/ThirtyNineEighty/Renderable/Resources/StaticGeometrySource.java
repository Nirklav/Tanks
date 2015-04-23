package com.ThirtyNineEighty.Renderable.Resources;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class StaticGeometrySource
  extends GeometrySource
{
  private FloatBuffer source;
  private int numOfTriangles;
  private MeshMode mode;

  public StaticGeometrySource(float[] src, int triangles, MeshMode md)
  {
    source = load(src);
    numOfTriangles = triangles;
    mode = md;
  }

  @Override
  public Geometry load()
  {
    switch (mode)
    {
    case Static:
      int handle = loadGeometry(source);
      return new Geometry(handle, numOfTriangles);
    case Dynamic:
      return new Geometry(source, numOfTriangles);
    }

    throw new IllegalArgumentException("Invalid mode");
  }

  @Override
  public void reload(Geometry geometry)
  {
    switch (mode)
    {
    case Static:
      int handle = loadGeometry(source);
      geometry.updateData(handle, numOfTriangles);
      return;
    case Dynamic:
      geometry.updateData(source, numOfTriangles);
      return;
    }

    throw new IllegalArgumentException("Invalid mode");
  }

  private static FloatBuffer load(float[] bufferData)
  {
    return (FloatBuffer) ByteBuffer.allocateDirect(bufferData.length * 4)
                                   .order(ByteOrder.nativeOrder())
                                   .asFloatBuffer()
                                   .put(bufferData)
                                   .position(0);
  }
}
