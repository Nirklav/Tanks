package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Common.Math.Vector3;

import java.io.Serializable;

public class Light
  implements Serializable
{
  public final Vector3 Position;

  public Light()
  {
    Position = new Vector3();
  }
}
