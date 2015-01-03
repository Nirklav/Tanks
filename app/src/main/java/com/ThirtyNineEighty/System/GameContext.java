package com.ThirtyNineEighty.System;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class GameContext
{
  public static final float EtalonHeight = 1080f;
  public static final float EtalonWidth  = 1920f;
  public static final float EtalonAspect = EtalonWidth / EtalonHeight;

  public static final float Left = EtalonWidth / -2f;
  public static final float Right = EtalonWidth / 2f;
  public static final float Bottom = EtalonHeight / -2f;
  public static final float Top = EtalonHeight / 2f;

  private static boolean isFirst;
  private static long delta;
  private static long lastTick;

  private static Context appContext;
  private static IContent content;

  private static boolean debuggable;

  private static float width;
  private static float height;

  public static Context getAppContext() { return appContext; }
  public static void setAppContext(Context value)
  {
    appContext = value;

    int flags = appContext.getApplicationInfo().flags;
    debuggable = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
  }

  public static IContent getContent() { return content; }
  public static void setContent(IContent value) { content = value; }

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

  public static float getDelta()
  {
    float d = delta / 1000.0f;

    if (d >= 1.0f)
      return 1.0f;

    return d;
  }

  public static boolean isDebuggable() { return debuggable; }
  public static float getAspect() { return width / height; }
  public static float getWidth() { return width; }
  public static void setWidth(float value) { width = value; }
  public static float getHeight() { return height; }
  public static void setHeight(float value) { height = value; }
}
