package com.ThirtyNineEighty.Helpers;

import java.util.ArrayDeque;

public class VectorCache
{
  private ArrayDeque<Vector> notUsed;

  public VectorCache()
  {
    notUsed = new ArrayDeque<Vector>() {};
  }

  public <TVector extends Vector> TVector getInstance(int vectorSize)
  {
    if (notUsed.isEmpty())
      return Vector.getInstance(vectorSize);

    return null;
  }
}
