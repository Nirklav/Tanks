package com.ThirtyNineEighty.Common;

import android.util.Log;

public class Stopwatch
{
  private long startMillis;
  private long criticalMillis;
  private String name;

  public Stopwatch(String watchName) { this (watchName, 100); }
  public Stopwatch(String watchName, long criticalMs)
  {
    startMillis = System.currentTimeMillis();
    name = watchName;
    criticalMillis = criticalMs;
  }

  public void stop()
  {
    long endMillis = System.currentTimeMillis();
    long elapsed = endMillis - startMillis;

    if (elapsed > criticalMillis)
    {
      Log.e("Stopwatch", String.format("%s; Elapsed: %d; Critical: %d;", name, elapsed, criticalMillis));
    }
  }
}
