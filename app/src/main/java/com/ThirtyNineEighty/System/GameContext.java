package com.ThirtyNineEighty.System;

import android.content.Context;

public class GameContext
{
  private static boolean isFirst;
  private static long delta;
  private static long lastTick;

  private static Context appContext;

  private static float width;
  private static float height;

  public static Context getAppContext()
  {
    return appContext;
  }

  public static void setAppContext(Context value)
  {
    appContext = value;
  }

  public static float updateTime()
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

  public static float getAspect()
  {
    return width / height;
  }

  public static float getDelta()
  {
    return delta / 1000.0f;
  }

  public static float getWidth()
  {
    return width;
  }

  public static void setWidth(float width)
  {
    GameContext.width = width;
  }

  public static float getHeight()
  {
    return height;
  }

  public static void setHeight(float height)
  {
    GameContext.height = height;
  }
}
