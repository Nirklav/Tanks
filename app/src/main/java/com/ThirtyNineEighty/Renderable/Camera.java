package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Helpers.Vector3;

public class Camera
{
  public final Vector3 eye;
  public final Vector3 target;

  public Camera()
  {
    eye = new Vector3();
    target = new Vector3();
  }
}
