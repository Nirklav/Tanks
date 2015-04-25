package com.ThirtyNineEighty.Renderable.Resources;

import java.nio.FloatBuffer;

public class StaticGeometrySource
  extends GeometrySource
{
  private FloatBuffer source;
  private int trianglesCount;

  public StaticGeometrySource(String name, float[] buffer, int triangles, MeshMode mode)
  {
    super(name, mode);
    source = loadGeometry(buffer);
    trianglesCount = triangles;
  }

  @Override
  public Geometry load()
  {
    switch (mode)
    {
    case Static:
      int handle = loadGeometry(source);
      return new Geometry(handle, trianglesCount);
    case Dynamic:
      return new Geometry(source, trianglesCount);
    }

    throw new IllegalArgumentException("Invalid mesh mode");
  }

  @Override
  public void reload(Geometry geometry)
  {
    release(geometry);

    switch (mode)
    {
    case Static:
      int handle = loadGeometry(source);
      geometry.updateData(handle, trianglesCount);
      return;
    case Dynamic:
      geometry.updateData(source, trianglesCount);
      return;
    }

    throw new IllegalArgumentException("Invalid mesh mode");
  }
}
