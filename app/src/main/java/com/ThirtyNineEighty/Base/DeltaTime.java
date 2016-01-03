package com.ThirtyNineEighty.Base;

public final class DeltaTime
{
  private static final float MinDelta = 1f / 15f; // 15 updates for 1 second

  private volatile static boolean isFirst;
  private volatile static long delta;
  private volatile static long lastTick;

  public static float update()
  {
    long currentTick;

    if (isFirst)
    {
      currentTick = System.currentTimeMillis();
      lastTick = currentTick;
      isFirst = false;
    }

    currentTick = System.currentTimeMillis();
    delta = currentTick - lastTick;
    lastTick = currentTick;

    return delta / 1000.0f;
  }

  public static float get()
  {
    float d = delta / 1000.0f;

    if (d >= MinDelta)
      return MinDelta;

    return d;
  }
}
