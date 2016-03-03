package com.ThirtyNineEighty.Base.Common.Math;

import java.io.Serializable;

public abstract class Vector
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  protected static final int poolLimit = 5000;
  public static final float epsilon = 0.0001f;

  protected int refCounter = 1;
  protected float[] value;

  public float get(int num)
  {
    throwIfReleased();
    return value[num];
  }

  public void set(int num, float v)
  {
    throwIfReleased();
    value[num] = v;
  }

  public void add(int num, float v)
  {
    throwIfReleased();
    value[num] += v;
  }

  public float[] getRaw()
  {
    throwIfReleased();
    return value;
  }

  public void setFrom(Vector vector)
  {
    throwIfReleased();

    int size = Math.min(getSize(), vector.getSize());
    System.arraycopy(vector.value, 0, value, 0, size);
  }

  public void correctAngles()
  {
    throwIfReleased();

    int size = getSize();
    for (int i = 0; i < size; i++)
    {
      float angle = Angle.correct(get(i));
      set(i, angle);
    }
  }

  public boolean isZero()
  {
    int size = getSize();
    for (int i = 0; i < size; i++)
    {
      if (Math.abs(get(i)) > epsilon)
        return false;
    }

    return true;
  }

  public abstract int getSize();
  public abstract void clear();

  protected void throwIfReleased()
  {
    if (refCounter != 1)
      throw new IllegalStateException("Reference counter should be equals one");
  }
}
