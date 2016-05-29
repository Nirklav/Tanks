package com.ThirtyNineEighty.Base.Common;

import android.util.Log;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Stopwatch
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private ThreadLocal<Long> startNano;

  private AtomicLong allNano;
  private AtomicLong maxNano;
  private AtomicLong calls;

  private long criticalMs;
  private String name;

  public Stopwatch(String watchName) { this (watchName, -1); }
  public Stopwatch(String watchName, long criticalMs)
  {
    this.name = watchName;
    this.criticalMs = criticalMs;
    this.startNano = new ThreadLocal<>();
    this.allNano = new AtomicLong();
    this.maxNano = new AtomicLong();
    this.calls = new AtomicLong();
  }

  public void start()
  {
    startNano.set(System.nanoTime());
  }

  public void stop()
  {
    long elapsedNano = elapsedNano();

    calls.incrementAndGet();
    allNano.addAndGet(elapsedNano);

    while (true)
    {
      long localMaxNano = maxNano.get();
      long result = localMaxNano;
      if (elapsedNano > result)
        result = elapsedNano;

      if (maxNano.compareAndSet(localMaxNano, result))
        break;
    }

    long elapsedMs = TimeUnit.NANOSECONDS.toMillis(elapsedNano);
    if (criticalMs > 0 && elapsedMs > criticalMs)
      Log.e("Stopwatch", String.format("%s; Elapsed: %d; Critical: %d;", name, elapsedMs, criticalMs));
  }

  private long elapsedNano()
  {
    long endNano = System.nanoTime();
    return endNano - startNano.get();
  }

  public long elapsed()
  {
    long nano = elapsedNano();
    return TimeUnit.NANOSECONDS.toMillis(nano);
  }

  public String name()
  {
    return name;
  }

  public long average()
  {
    long localCalls = calls.get();
    long localAllNano = allNano.get();

    if (localCalls == 0)
      return 0;

    long nano = localAllNano / localCalls;
    return TimeUnit.NANOSECONDS.toMillis(nano);
  }

  public long max()
  {
    return TimeUnit.NANOSECONDS.toMillis(maxNano.get());
  }

  public long all()
  {
    return TimeUnit.NANOSECONDS.toMillis(allNano.get());
  }

  public long calls()
  {
    return calls.get();
  }

  @Override
  public String toString()
  {
    return name + ": (" + average() + "/" + max() + ")";
  }
}
