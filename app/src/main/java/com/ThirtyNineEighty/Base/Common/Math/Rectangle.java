package com.ThirtyNineEighty.Base.Common.Math;

public class Rectangle
{
  private Vector2 leftBottom;
  private Vector2 topRight;

  public Rectangle(Vector2 point1, Vector2 point2, float minLength)
  {
    leftBottom = Vector2.getInstance();
    topRight = Vector2.getInstance();

    Vector2 diff = point1.getSubtract(point2);

    int size = diff.getSize();
    for (int i = 0; i < size; i++)
    {
      float diffValue = diff.get(i);

      float addValue = 0;
      if (Math.abs(diffValue) < minLength)
        addValue = (minLength - Math.abs(diffValue)) / 2;

      if (Math.abs(diffValue) < Vector.epsilon)
      {
        float value = point1.get(i);
        topRight.set(i, value + addValue);
        leftBottom.set(i, value - addValue);
      }

      if (diffValue < 0)
      {
        topRight.set(i, point2.get(i) + addValue);
        leftBottom.set(i, point1.get(i) - addValue);
      }

      if (diffValue > 0)
      {
        topRight.set(i, point1.get(i) + addValue);
        leftBottom.set(i, point2.get(i) - addValue);
      }
    }
  }

  public Vector2 getLeftBottom() { return leftBottom; }
  public Vector2 getTopRight() { return topRight; }

  public boolean contains(Vector2 position)
  {
    return isBetween(position.getX(), leftBottom.getX(), topRight.getX())
        && isBetween(position.getY(), leftBottom.getY(), topRight.getY());
  }

  private static boolean isBetween(float value, float min, float max)
  {
    return value > min && value < max;
  }
}
