package com.ThirtyNineEighty.Base;

import java.util.concurrent.TimeUnit;

public final class DeltaTime
{
  private static final float MinDelta = 1f / 5f; // 5 updates for 1 second

  private volatile static boolean isFirst;
  private volatile static long deltaNano;
  private volatile static long lastTimeNano;

  public static float update()
  {
    long currentTimeNano;

    if (isFirst)
    {
      currentTimeNano = System.nanoTime();
      lastTimeNano = currentTimeNano;
      isFirst = false;
    }

    currentTimeNano = System.nanoTime();
    deltaNano = currentTimeNano - lastTimeNano;
    lastTimeNano = currentTimeNano;

    return deltaNano / 1000.0f;
  }

  public static float get()
  {
    float deltaMillis = TimeUnit.NANOSECONDS.toMillis(deltaNano);
    float d = deltaMillis / 1000.0f;

    if (d >= MinDelta)
      return MinDelta;

    return d;
  }
}
