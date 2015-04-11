package com.ThirtyNineEighty.Helpers;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
public abstract class Vector
{
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
        throw new IllegalStateException("Can't add this vector in cache. Wrong size.");

      vector.clear();
      vectors.add(vector);
    }

    public Vector get() { return vectors.poll(); }
    public int getCacheSize() { return vectors.size(); }
    public int getVectorSize() { return size; }
  }

  protected static final float epsilon = 0.0001f;

  private static final int cacheSizeLimit = 500;
  private static final boolean disableCache = false;
  private static final SparseArray<Vectors> cache = new SparseArray<Vectors>();
  private static final AtomicInteger cacheSize = new AtomicInteger();

  protected boolean released;

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
    if (disableCache)
      return create(vectorSize);

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
        vector.released = false;
        return (TVector) vector;
      }
    }

    return create(vectorSize);
  }

  @SuppressWarnings({ "unchecked" })
  private static <TVector extends Vector> TVector create(int vectorSize)
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

  public static <TVector extends Vector> void release(Collection<TVector> vectorsCollection)
  {
    if (disableCache)
      return;

    if (vectorsCollection.size() == 0)
      return;

    int addedCount = tryAddCacheSize(vectorsCollection.size());
    if (addedCount == 0)
      return;

    int vectorSize = -1;
    //noinspection LoopStatementThatDoesntLoop
    for(TVector first : vectorsCollection)
    {
      vectorSize = first.getSize();
      break;
    }

    if (vectorSize == -1)
      return;

    Vectors vectors;
    synchronized (cache)
    {
      vectors = cache.get(vectorSize);
      if (vectors == null)
        cache.append(vectorSize, vectors = new Vectors(vectorSize));
    }

    synchronized (vectors)
    {
      for(TVector vector : vectorsCollection)
      {
        vector.released = true;
        vectors.release(vector);
      }
    }
  }

  public static void release(Vector vector)
  {
    if (disableCache)
      return;

    if (tryAddCacheSize(1) == 0)
      return;

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
      vector.released = true;
      vectors.release(vector);
    }
  }

  private static int tryAddCacheSize(int value)
  {
    while (true)
    {
      int cachedValue = cacheSize.get();
      int changedValue = Math.min(cachedValue + value, cacheSizeLimit);

      if (changedValue >= cacheSizeLimit)
        return 0;

      if (cacheSize.compareAndSet(cachedValue, changedValue))
        return changedValue - cachedValue;
    }
  }

  public static String getCacheStatus()
  {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("[Cache size: %s]\n", cacheSize.get()));
    synchronized (cache)
    {
      int size = cache.size();
      for (int i = 0; i < size; i++)
      {
        int key = cache.keyAt(i);
        Vectors vectors = cache.get(key);

        builder.append(String.format("[Size: %s, VectorSize: %s] \n", vectors.getCacheSize(), vectors.getVectorSize()));
      }
    }

    return builder.toString();
  }

  public abstract float get(int num);
  public abstract void set(int num, float value);
  public abstract int getSize();
  public abstract void clear();

  public void correctAngles()
  {
    for (int i = 0; i < getSize(); i++)
    {
      float angle = Angle.correct(get(i));
      set(i, angle);
    }
  }

  protected void throwIfReleased()
  {
    if (released)
      throw new IllegalStateException("Vector released not use it.");
  }
}
