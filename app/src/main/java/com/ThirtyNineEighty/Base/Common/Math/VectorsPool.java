package com.ThirtyNineEighty.Base.Common.Math;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class VectorsPool<TVector extends Vector>
{
  private final ConcurrentLinkedQueue<TVector> pool;
  private final AtomicInteger poolSize;
  private final int limit;

  public VectorsPool(int limit)
  {
    this.pool = new ConcurrentLinkedQueue<>();
    this.poolSize = new AtomicInteger();
    this.limit = limit;
  }

  public TVector acquire()
  {
    TVector vector = pool.poll();
    if (vector != null)
    {
      poolSize.decrementAndGet();
      vector.refCounter ++;
    }
    return vector;
  }

  public void release(TVector vector)
  {
    if (vector == null)
      return;

    clear(vector);

    if (addSize(1) == 0)
      return;

    pool.offer(vector);
  }

  public void release(Collection<TVector> vectors)
  {
    if (vectors == null)
      return;

    int size = vectors.size();
    int toCache = addSize(size);

    for (TVector vector : vectors)
    {
      clear(vector);
      if (toCache > 0)
        pool.offer(vector);
      toCache--;
    }
  }

  private void clear(TVector vector)
  {
    if (vector.refCounter <= 0)
      throw new IllegalStateException("Vector already released");

    vector.refCounter --;
    vector.clear();
  }

  private int addSize(int value)
  {
    while (true)
    {
      int size = poolSize.get();
      if (size >= limit)
        return 0;

      int changedSize = Math.min(size + value, limit);

      if (poolSize.compareAndSet(size, changedSize))
        return changedSize - size;
    }
  }
}
