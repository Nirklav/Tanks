package com.ThirtyNineEighty.Renderable.Resources;

import java.nio.FloatBuffer;

public class StaticGeometrySource
  extends GeometrySource
{
  private FloatBuffer buffer;
  private int trianglesCount;

  public StaticGeometrySource(String name, float[] buffer, int trianglesCount, MeshMode mode) { this(name, loadGeometry(buffer), trianglesCount, mode); }
  public StaticGeometrySource(String name, FloatBuffer buffer, int trianglesCount, MeshMode mode)
  {
    super(name, mode);
    this.buffer = buffer;
    this.trianglesCount = trianglesCount;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    return new LoadResult(buffer, trianglesCount);
  }
}
