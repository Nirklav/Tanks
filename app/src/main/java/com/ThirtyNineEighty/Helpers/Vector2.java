package com.ThirtyNineEighty.Helpers;

public class Vector2
{
  public static Vector2 xAxis = new Vector2(1.0f, 0.0f);
  public static Vector2 yAxis = new Vector2(0.0f, 1.0f);

  protected float[] value;

  public Vector2()
  {
    this(0, 0);
  }

  public Vector2(float x, float y)
  {
    value = new float[2];
    value[0] = x;
    value[1] = y;
  }

  public Vector2(Vector2 vec)
  {
    this(vec.getX(), vec.getY());
  }

  public float getX() { return value[0]; }
  public float getY() { return value[1]; }

  public void setX(float v) { value[0] = v; }
  public void setY(float v) { value[1] = v; }

  public void addToX(float v) { value[0] += v; }
  public void addToY(float v) { value[1] += v; }

  public float[] getRaw() { return value; }

  public float getLength()
  {
    double powX = Math.pow(getX(), 2);
    double powY = Math.pow(getY(), 2);

    return (float)Math.sqrt(powX + powY);
  }

  public void normalize()
  {
    float length = getLength();

    value[0] /= length;
    value[1] /= length;
  }

  public float getAngle(Vector2 other)
  {
    float scalar = getScalar(other);
    float lengthOne = this.getLength();
    float lengthTwo = other.getLength();
    float angle = (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));

    return getCross(other) > 0 ? angle : angle - 360;
  }

  public float getScalar(Vector2 other)
  {
    float multOne   = getX() * other.getX();
    float multTwo   = getY() * other.getY();

    return multOne + multTwo;
  }

  public float getCross(Vector2 other)
  {
    return getX() * other.getY() - getY() * other.getX();
  }

  public Vector2 subtract(Vector2 other)
  {
    return new Vector2
    (
      getX() - other.getX(),
      getY() - other.getY()
    );
  }

  @Override
  public boolean equals(Object o)
  {
    Vector2 other = o instanceof Vector2 ? (Vector2)o : null;

    return other != null
      && other.getX() == getX()
      && other.getY() == getY();
  }

  @Override
  public String toString()
  {
    return String.format("{%f; %f}", value[0], value[1]);
  }
}
