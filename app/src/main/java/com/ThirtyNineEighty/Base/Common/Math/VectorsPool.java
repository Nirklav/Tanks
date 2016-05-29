package com.ThirtyNineEighty.Base.Common.Math;

import com.ThirtyNineEighty.Base.IStatistics;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VectorsPool<TVector extends Vector>
  implements IStatistics
{
  // statistics
  private final AtomicLong allAcquireCounter;
  private final AtomicLong successfulAcquireCounter;

  // data
  private final String name;
  private final ConcurrentLinkedQueue<TVector> pool;
  private final AtomicInteger poolSize;
  private final int limit;

  public VectorsPool(String name, int limit)
  {
    this.allAcquireCounter = new AtomicLong();
    this.successfulAcquireCounter = new AtomicLong();

    this.name = name;
    this.pool = new ConcurrentLinkedQueue<>();
    this.poolSize = new AtomicInteger();
    this.limit = limit;
  }

  public TVector acquire()
  {
    allAcquireCounter.incrementAndGet();

    TVector vector = pool.poll();
    if (vector != null)
    {
      successfulAcquireCounter.incrementAndGet();
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

    vectors.clear();
  }

  public String getStatistics()
  {
    return String.format("[%s]\nall: %d\nsuccessful: %d\nsize: %d\n"
      , name
      , allAcquireCounter.get()
      , successfulAcquireCounter.get()
      , poolSize.get()
    );
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
