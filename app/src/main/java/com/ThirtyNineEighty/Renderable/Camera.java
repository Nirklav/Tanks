package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Common.Math.Vector3;

import java.io.Serializable;

public class Camera
  implements Serializable
{
  public final Vector3 eye;
  public final Vector3 target;

  public Camera()
  {
    eye = new Vector3();
    target = new Vector3();
  }
}
