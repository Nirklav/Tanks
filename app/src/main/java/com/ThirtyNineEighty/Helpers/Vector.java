package com.ThirtyNineEighty.Helpers;

import android.util.Log;

public abstract class Vector
{
  protected static final float epsilon = 0.0001f;

  public static <TVector extends Vector> TVector getInstance(int vectorSize)
  {
    try
    {
      switch (vectorSize)
      {
      case 2:
        return (TVector)new Vector2();
      case 3:
      case 4:
        return (TVector)new Vector3();
      default:
        return null;
      }
    }
    catch (Exception e)
    {
      Log.e("Vector", e.getMessage());
      return null;
    }
  }

  public abstract float get(int num);
  public abstract void set(int num, float value);
}
