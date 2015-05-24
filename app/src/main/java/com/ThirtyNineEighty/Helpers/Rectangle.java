package com.ThirtyNineEighty.Helpers;

/**
 * Created by Admin on 24.05.2015.
 */
public class Rectangle
{
  private Vector2 start;
  private Vector2 end;

  public Rectangle(Vector2 start, Vector2 end)
  {
    this.start = start;
    this.end = end;
  }

  public Vector2 getStart() { return start; }
  public Vector2 getEnd() { return end; }

  public boolean contains(Vector2 position)
  {
    return isBetween(position.getX(), start.getX(), end.getX())
        && isBetween(position.getY(), start.getY(), end.getY());
  }

  private static boolean isBetween(float value, float left, float right)
  {
    return value > left && value < right;
  }
}
