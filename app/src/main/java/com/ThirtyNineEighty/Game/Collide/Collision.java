package com.ThirtyNineEighty.Game.Collide;

public abstract class Collision
{
  protected float[] mtv;
  protected float mtvLength;
  protected boolean collide;

  public float[] getMTV()
  {
    return mtv;
  }

  public float getMTVLength()
  {
    return mtvLength;
  }

  public boolean isCollide()
  {
    return collide;
  }

  public static int compare(Collision first, Collision second)
  {
    if (first.mtvLength < second.mtvLength)
      return -1;

    if (first.mtvLength > second.mtvLength)
      return 1;

    return 0;
  }
}
