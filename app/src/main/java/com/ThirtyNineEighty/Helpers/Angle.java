package com.ThirtyNineEighty.Helpers;

public final class Angle
{
  public static float correct(float angle)
  {
    while ( angle < 0.0f || angle >= 360.0f)
    {
      if (angle < 0.0f)
        angle += 360.0f;

      if (angle >= 360.0f)
        angle -= 360.0f;
    }

    return angle;
  }
}
