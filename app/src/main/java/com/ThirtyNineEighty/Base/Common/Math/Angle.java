package com.ThirtyNineEighty.Base.Common.Math;

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

  public static int getDirection(float angle, float targetAngle)
  {
    if (Math.abs(angle - targetAngle) > Vector.epsilon)
    {
      float tempValue = (angle - targetAngle < 0)
        ? angle - targetAngle + 360
        : angle - targetAngle;

      return (tempValue < 180) ? -1 : 1;
    }
    return 0;
  }
}
