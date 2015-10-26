package com.ThirtyNineEighty.System;

import android.content.pm.ApplicationInfo;

import com.ThirtyNineEighty.Game.Collisions.CollisionManager;
import com.ThirtyNineEighty.Game.Data.GameProgressManager;
import com.ThirtyNineEighty.Game.Factory.Factory;
import com.ThirtyNineEighty.Game.Map.MapManager;
import com.ThirtyNineEighty.Renderable.IRenderer;
import com.ThirtyNineEighty.Resources.Resources;

public class GameContext
{
  private static final float MinDelta = 1f / 15f; // 15 updates for 1 second

  // Width / Height
  public static final float EtalonHeight = 1080f;
  public static final float EtalonWidth  = 1920f;
  public static final float EtalonAspect = EtalonWidth / EtalonHeight;

  public static final float Left = EtalonWidth / -2f;
  public static final float Right = EtalonWidth / 2f;
  public static final float Bottom = EtalonHeight / -2f;
  public static final float Top = EtalonHeight / 2f;

  private static float width;
  private static float height;

  public static float getAspect() { return width / height; }
  public static float getWidth() { return width; }
  public static void setWidth(float value) { width = value; }
  public static float getHeight() { return height; }
  public static void setHeight(float value) { height = value; }

  // Debuggable
  private static boolean debuggable;
  public static boolean isDebuggable() { return debuggable; }

  // System
  private static long mainThreadId;
  public static void setMainThread() { mainThreadId = Thread.currentThread().getId(); }
  public static boolean isMainThread() { return mainThreadId == Thread.currentThread().getId(); }

  private static long glThreadId;
  public static void setGLThread() { glThreadId = Thread.currentThread().getId(); }
  public static boolean isGLThread() { return glThreadId == Thread.currentThread().getId(); }

  public static GameActivity activity;
  public static Content content;
  public static IRenderer renderer;

  public static void setActivity(GameActivity value)
  {
    activity = value;

    if (activity != null)
    {
      int flags = activity.getApplicationInfo().flags;
      debuggable = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
  }

  // Helpers
  public static final MapManager mapManager = new MapManager();
  public static final Resources resources = new Resources();
  public static final CollisionManager collisions = new CollisionManager();
  public static final GameProgressManager gameProgress = new GameProgressManager();
  public static final Factory factory = new Factory();

  // Delta time
  private static boolean isFirst;
  private static long delta;
  private static long lastTick;

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

    if (d >= MinDelta)
      return MinDelta;

    return d;
  }
}
