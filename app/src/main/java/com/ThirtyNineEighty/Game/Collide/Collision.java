package com.ThirtyNineEighty.Game.Collide;

public abstract class Collision<T>
{
  public abstract T getMTV();
  public abstract float getMTVLength();
  public abstract boolean isCollide();

  public static int compare(Collision first, Collision second)
  {
    if (first.getMTVLength() < second.getMTVLength())
      return -1;
    if (first.getMTVLength() > second.getMTVLength())
      return 1;
    return 0;
  }
}
