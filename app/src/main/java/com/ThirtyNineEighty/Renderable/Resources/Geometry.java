package com.ThirtyNineEighty.Renderable.Resources;

import java.nio.FloatBuffer;

public class Geometry
{
  private MeshMode mode;
  private int handle;
  private int trianglesCount;
  private FloatBuffer data;

  public Geometry(int handle, int trianglesCount)
  {
    this.handle = handle;
    this.trianglesCount = trianglesCount;
    this.mode = MeshMode.Static;
    this.data = null;
  }

  public Geometry(FloatBuffer data, int trianglesCount)
  {
    this.handle = 0;
    this.trianglesCount = trianglesCount;
    this.mode = MeshMode.Dynamic;
    this.data = data;
  }

  public MeshMode getMode() { return mode; }

  public int getTrianglesCount() { return trianglesCount; }
  public int getHandle() { return handle; }

  public FloatBuffer getData()
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");
    return data;
  }

  public void updateData(int handle, int trianglesCount)
  {
    if (mode != MeshMode.Static)
      throw new IllegalStateException("not right mode");

    this.handle = handle;
    this.trianglesCount = trianglesCount;
  }

  public void updateData(FloatBuffer data, int trianglesCount)
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");

    this.data = data;
    this.trianglesCount = trianglesCount;
  }
}
