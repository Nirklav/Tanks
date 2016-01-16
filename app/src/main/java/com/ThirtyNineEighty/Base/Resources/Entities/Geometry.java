package com.ThirtyNineEighty.Base.Resources.Entities;

import android.opengl.GLES20;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.GameContext;

import java.nio.FloatBuffer;

public class Geometry
  extends Resource
{
  private MeshMode mode;

  private int handle;
  private FloatBuffer data;

  private int pointsCount;
  private Vector3 position;
  private Vector3 angles;

  public Geometry(String name, int handle, int pointsCount, Vector3 position, Vector3 angles)
  {
    super(name);

    this.handle = handle;
    this.pointsCount = pointsCount;
    this.mode = MeshMode.Static;
    this.data = null;

    this.position = new Vector3(position);
    this.angles = new Vector3(angles);
  }

  public Geometry(String name, FloatBuffer data, int pointsCount, Vector3 position, Vector3 angles)
  {
    super(name);

    this.handle = 0;
    this.pointsCount = pointsCount;
    this.mode = MeshMode.Dynamic;
    this.data = data;

    this.position = new Vector3(position);
    this.angles = new Vector3(angles);
  }

  public MeshMode getMode() { return mode; }

  public int getHandle() { return handle; }
  public FloatBuffer getData()
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");
    return data;
  }

  public int getPointsCount() { return pointsCount; }
  public Vector3 getPosition() { return position; }
  public Vector3 getAngles() { return angles; }

  public void updateData(int handle, int trianglesCount, Vector3 position, Vector3 angles)
  {
    if (mode != MeshMode.Static)
      throw new IllegalStateException("not right mode");

    this.handle = handle;
    this.pointsCount = trianglesCount;

    this.position = position == null ? null : new Vector3(position);
    this.angles = angles == null ? null : new Vector3(angles);
  }

  public void updateData(FloatBuffer data, int trianglesCount, Vector3 position, Vector3 angles)
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");

    this.data = data;
    this.pointsCount = trianglesCount;
    this.position = position == null ? null : new Vector3(position);
    this.angles = angles == null ? null : new Vector3(angles);
  }

  public void validate()
  {
    if (!GameContext.debuggable || mode != MeshMode.Static)
      return;

    if (!GLES20.glIsBuffer(handle))
      throw new IllegalStateException("Buffer handle deprecated");
  }
}
