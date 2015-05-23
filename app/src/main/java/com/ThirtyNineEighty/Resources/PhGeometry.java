package com.ThirtyNineEighty.Resources;

import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.ArrayList;

public class PhGeometry
{
  public final ArrayList<Vector3> vertices;
  public final ArrayList<Vector3> normals;

  public final Vector3 position;
  public final Vector3 angles;

  public final float radius;

  public PhGeometry(ArrayList<Vector3> vertices, ArrayList<Vector3> normals, Vector3 position, Vector3 angles, float radius)
  {
    this.vertices = vertices;
    this.normals = normals;
    this.position = position;
    this.angles = angles;
    this.radius = radius;
  }
}
