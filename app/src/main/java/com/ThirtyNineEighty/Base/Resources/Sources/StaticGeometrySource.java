package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

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
    return new LoadResult(buffer, trianglesCount * 3, Vector3.zero, Vector3.zero);
  }
}
