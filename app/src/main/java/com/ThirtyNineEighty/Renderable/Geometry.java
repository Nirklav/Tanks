package com.ThirtyNineEighty.Renderable;

import java.nio.FloatBuffer;

public class Geometry
{
  private MeshMode mode;
  private int handle;
  private int numOfTriangles;
  private FloatBuffer data;

  public Geometry(int handle, int numOfTriangles)
  {
    this.handle = handle;
    this.numOfTriangles = numOfTriangles;
    this.mode = MeshMode.Static;
    this.data = null;
  }

  public Geometry(FloatBuffer data, int numOfTriangles)
  {
    this.handle = 0;
    this.numOfTriangles = numOfTriangles;
    this.mode = MeshMode.Dynamic;
    this.data = data;
  }

  public MeshMode getMode() { return mode; }

  public int getNumOfTriangles() { return numOfTriangles; }
  public int getHandle() { return handle; }

  public FloatBuffer getData()
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");
    return data;
  }

  private void updateData(int handle, int numOfTriangles)
  {
    if (mode != MeshMode.Static)
      throw new IllegalStateException("not right mode");

    this.handle = handle;
    this.numOfTriangles = numOfTriangles;
  }

  private void updateData(FloatBuffer data, int numOfTriangles)
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");

    this.data = data;
    this.numOfTriangles = numOfTriangles;
  }
}
