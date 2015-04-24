package com.ThirtyNineEighty.Renderable.Resources;

import java.nio.FloatBuffer;

public class StaticGeometrySource
  extends GeometrySource
{
  private FloatBuffer source;
  private int numOfTriangles;

  public StaticGeometrySource(String name, float[] buffer, int trianglesCount, MeshMode mode)
  {
    super(name, mode);
    source = loadGeometry(buffer);
    numOfTriangles = trianglesCount;
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
      geometry.updateData(handle, numOfTriangles);
      return;
    case Dynamic:
      geometry.updateData(source, numOfTriangles);
      return;
    }

    throw new IllegalArgumentException("Invalid mesh mode");
  }
}
