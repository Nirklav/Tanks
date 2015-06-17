package com.ThirtyNineEighty.Helpers;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Operation with prefix get - immutable;
 */
public class Vector2 extends Vector
{
  public final static Vector2 xAxis = new Vector2(1.0f, 0.0f);
  public final static Vector2 yAxis = new Vector2(0.0f, 1.0f);

  protected float[] value;

  public Vector2()
  {
    value = new float[2];
  }

  public Vector2(float x, float y)
  {
    this();
    setFrom(x, y);
  }

  public Vector2(float[] raw)
  {
    value = raw;
  }

  public Vector2(Vector2 vec)
  {
    this();
    setFrom(vec);
  }

  public void setFrom(float x, float y)
  {
    throwIfReleased();

    value[0] = x;
    value[1] = y;
  }

  public void setFrom(Vector2 vec)
  {
    throwIfReleased();

    setFrom(vec.getX(), vec.getY());
  }

  public float getX() { throwIfReleased(); return value[0]; }
  public float getY() { throwIfReleased(); return value[1]; }

  public void setX(float v) { throwIfReleased(); value[0] = v; }
  public void setY(float v) { throwIfReleased(); value[1] = v; }

  public void addToX(float v) { throwIfReleased(); value[0] += v; }
  public void addToY(float v) { throwIfReleased(); value[1] += v; }

  public void multiplyToX(float v) { throwIfReleased(); value[0] *= v; }
  public void multiplyToY(float v) { throwIfReleased(); value[1] *= v; }

  public float[] getRaw() { throwIfReleased(); return value; }

  public float getLength()
  {
    throwIfReleased();

    double powX = Math.pow(getX(), 2);
    double powY = Math.pow(getY(), 2);

    return (float)Math.sqrt(powX + powY);
  }

  public float getAngle(Vector2 other)
  {
    throwIfReleased();

    float scalar = getScalar(other);
    float lengthOne = this.getLength();
    float lengthTwo = other.getLength();
    float angle = (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));

    return Angle.correct(getCross(other) > 0 ? angle : 360 - angle);
  }

  public float getScalar(Vector2 other)
  {
    throwIfReleased();

    float multOne = getX() * other.getX();
    float multTwo = getY() * other.getY();

    return multOne + multTwo;
  }

  public float getCross(Vector2 other)
  {
    throwIfReleased();

    return getX() * other.getY() - getY() * other.getX();
  }

  public void normalize()
  {
    throwIfReleased();

    float length = getLength();

    value[0] /= length;
    value[1] /= length;
  }

  public void subtract(Vector2 other)
  {
    throwIfReleased();

    setFrom(getX() - other.getX(), getY() - other.getY());
  }

  public void add(Vector2 other)
  {
    throwIfReleased();

    setFrom(getX() + other.getX(), getY() + other.getY());
  }

  public Vector2 getNormalize()
  {
    throwIfReleased();

    Vector2 result = Vector.getInstance(2, this);
    result.normalize();
    return result;
  }

  public void move(float length, float angle)
  {
    throwIfReleased();

    value[0] += length * Math.cos(Math.toRadians(angle));
    value[1] += length * Math.sin(Math.toRadians(angle));
  }

  public Vector2 getSubtract(Vector2 other)
  {
    throwIfReleased();

    Vector2 result = Vector.getInstance(2, this);
    result.subtract(other);
    return result;
  }

  public Vector2 getSum(Vector2 other)
  {
    throwIfReleased();

    Vector2 result = Vector.getInstance(2, this);
    result.add(other);
    return result;
  }

  public Vector2 getMove(float length, float angle)
  {
    throwIfReleased();

    Vector2 result = Vector.getInstance(2, this);
    result.move(length, angle);
    return result;
  }

  public Vector2 getProjection(ArrayList<Vector2> vertices)
  {
    Vector2 result = null;

    for (Vector2 current : vertices)
    {
      float projection = getScalar(current);

      if (result == null)
        result = new Vector2(projection, projection);

      // x - max
      if (projection > result.getX())
        result.setX(projection);

      // y - min
      if (projection < result.getY())
        result.setY(projection);
    }

    return result;
  }

  @Override
  public boolean equals(Object o)
  {
    throwIfReleased();

    Vector2 other = o instanceof Vector2 ? (Vector2)o : null;

    return other != null
      && Math.abs(other.getX() - getX()) < epsilon
      && Math.abs(other.getY() - getY()) < epsilon;
  }

  @Override
  public int hashCode()
  {
    throwIfReleased();
    return Arrays.hashCode(value);
  }

  @Override
  public String toString()
  {
    throwIfReleased();
    return String.format("{%f; %f}", value[0], value[1]);
  }

  @Override
  public float get(int num)
  {
    throwIfReleased();
    return value[num];
  }

  @Override
  public void set(int num, float v)
  {
    throwIfReleased();
    value[num] = v;
  }

  @Override
  public void clear()
  {
    value[0] = 0;
    value[1] = 0;
  }

  @Override
  public int getSize()
  {
    return 2;
  }
}
