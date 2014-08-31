package com.ThirtyNineEighty.System;

public class DeltaTime
{
  private static boolean isFirst;
  private static long delta;
  private static long lastTick;

  public static float UpdateTime()
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

  public static float getDelta()
  {
    return delta / 1000.0f;
  }
}
