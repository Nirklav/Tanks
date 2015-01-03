package com.ThirtyNineEighty.Helpers;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
public abstract class Vector
{
  //region nested type

  private static class Vectors
  {
    private ArrayDeque<Vector> vectors;
    private int size;

    public Vectors(int size)
    {
      vectors = new ArrayDeque<Vector>();
      this.size = size;
    }

    public void release(Vector vector)
    {
      if (vector.getSize() != size)
        return;

      vectors.add(vector);
    }

    public Vector get() { return vectors.poll(); }
    public int getCacheSize() { return vectors.size(); }
    public int getVectorSize() { return size; }
  }

  //endregion

  protected static final float epsilon = 0.0001f;

  // region cache

  private static final int cacheSizeLimit = 1000;
  private static final SparseArray<Vectors> cache = new SparseArray<Vectors>();
  private static final AtomicInteger cacheSize = new AtomicInteger();

  public static <TVector extends Vector> TVector getInstance(int vectorSize, TVector copy)
  {
    TVector vector = getInstance(vectorSize);

    for(int i = 0; i < vectorSize; i++)
    {
      float value = copy.get(i);
      vector.set(i, value);
    }

    return vector;
  }

  @SuppressWarnings({ "unchecked" })
  public static <TVector extends Vector> TVector getInstance(int vectorSize)
  {
    Vectors vectors;
    synchronized (cache)
    {
      vectors = cache.get(vectorSize);
      if (vectors == null)
        cache.append(vectorSize, vectors = new Vectors(vectorSize));
    }

    synchronized (vectors)
    {
      Vector vector = vectors.get();
      if (vector != null)
      {
        cacheSize.decrementAndGet();
        return (TVector) vector;
      }
    }

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

  public static <TVector extends Vector> void release(Collection<TVector> vectors)
  {
    for(TVector vec : vectors)
      release(vec);
  }

  public static void release(Vector vector)
  {
    if (cacheSize.get() >= cacheSizeLimit)
      return;

    cacheSize.incrementAndGet();

    int size = vector.getSize();
    Vectors vectors;
    synchronized (cache)
    {
      vectors = cache.get(size);
      if (vectors == null)
        cache.append(size, vectors = new Vectors(size));
    }

    synchronized (vectors)
    {
      vectors.release(vector);
    }
  }

  public static String getCacheStatus()
  {
    StringBuilder builder = new StringBuilder();
    synchronized (cache)
    {
      int size = cache.size();
      for (int i = 0; i < size; i++)
      {
        int key = cache.keyAt(i);
        Vectors vectors = cache.get(key);

        builder.append(String.format("[Size: %s, VectorSize: %s] /n", vectors.getCacheSize(), vectors.getVectorSize()));
      }
    }

    return builder.toString();
  }

  //endregion

  public abstract float get(int num);
  public abstract void set(int num, float value);
  public abstract int getSize();
}
