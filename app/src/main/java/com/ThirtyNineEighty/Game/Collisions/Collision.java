package com.ThirtyNineEighty.Game.Collisions;

public abstract class Collision<T>
{
  public abstract T getMTV();
  public abstract float getMTVLength();
  public abstract boolean isCollide();

  public static int compare(Collision first, Collision second)
  {
    float firstLength = first.getMTVLength();
    float secondLength = second.getMTVLength();
    return Float.compare(firstLength, secondLength);
  }
}
