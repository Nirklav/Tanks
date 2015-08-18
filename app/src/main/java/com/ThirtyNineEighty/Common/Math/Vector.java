package com.ThirtyNineEighty.Common.Math;

import android.util.SparseArray;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
public abstract class Vector
  implements Serializable
{
  private static class Vectors
  {
    private ArrayDeque<Vector> vectors;
    private int size;

    public Vectors(int size)
    {
      vectors = new ArrayDeque<>();
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

  public static final float epsilon = 0.0001f;

  private static final long serialVersionUID = 1L;

  private static final int cacheSizeLimit = 500;
  private static final boolean disableCache = false;
  private static final SparseArray<Vectors> cache = new SparseArray<>();
  private static final AtomicInteger cacheSize = new AtomicInteger();

  protected boolean released;
  protected float[] value;

  public static <TVector extends Vector> TVector getInstance(int vectorSize, Vector copy)
  {
    TVector vector = getInstance(vectorSize);
    vector.setFrom(copy);
    return vector;
  }

  public static <TVector extends Vector> TVector getInstance(int vectorSize, float ...values)
  {
    TVector vector = getInstance(vectorSize);
    for (int i = 0; i < values.length; i++)
      vector.set(i, values[i]);
    return vector;
  }

  public static <TVector extends Vector> TVector getInstance(int vectorSize, ByteBuffer dataBuffer)
  {
    TVector vector = getInstance(vectorSize);
    for (int i = 0; i < vectorSize; i++)
      vector.set(i, dataBuffer.getFloat());

    return vector;
  }

  @SuppressWarnings("unchecked")
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

  @SuppressWarnings("unchecked")
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
      throw new RuntimeException(e);
    }
  }

  public static <TVector extends Vector> void release(Collection<TVector> vectorsCollection)
  {
    if (vectorsCollection == null)
      return;

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
    if (vector == null)
      return;

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

  public float[] getRaw() { throwIfReleased(); return value; }

  public void setFrom(Vector vector)
  {
    throwIfReleased();

    int size = Math.min(getSize(), vector.getSize());
    for (int i = 0; i < size; i++)
      set(i, vector.get(i));
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

  protected void throwIfReleased()
  {
    if (released)
      throw new IllegalStateException("Vector released not use it.");
  }
}
