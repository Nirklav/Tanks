package com.ThirtyNineEighty.Base.Common;

import android.util.Log;

import java.io.Serializable;

public class Stopwatch
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private long startMillis;
  private long criticalMillis;
  private String name;

  public Stopwatch(String watchName) { this (watchName, 100); }
  public Stopwatch(String watchName, long criticalMs)
  {
    name = watchName;
    criticalMillis = criticalMs;
  }

  public long elapsed()
  {
    long endMillis = System.currentTimeMillis();
    return endMillis - startMillis;
  }

  public void start()
  {
    startMillis = System.currentTimeMillis();
  }

  public void stop()
  {
    long endMillis = System.currentTimeMillis();
    long elapsed = endMillis - startMillis;

    if (elapsed > criticalMillis)
      Log.e("Stopwatch", String.format("%s; Elapsed: %d; Critical: %d;", name, elapsed, criticalMillis));
  }
}
