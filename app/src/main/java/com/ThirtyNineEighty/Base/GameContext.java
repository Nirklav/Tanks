package com.ThirtyNineEighty.Base;

import android.content.Context;

import com.ThirtyNineEighty.Base.Collisions.CollisionManager;
import com.ThirtyNineEighty.Base.Data.DataManager;
import com.ThirtyNineEighty.Base.Factory.Factory;
import com.ThirtyNineEighty.Base.Renderable.IRenderer;
import com.ThirtyNineEighty.Base.Resources.Resources;

import java.util.concurrent.ExecutorService;

public class GameContext
{
  private static long mainThreadId;
  public static void setMainThread() { mainThreadId = Thread.currentThread().getId(); }
  public static boolean isMainThread() { return mainThreadId == Thread.currentThread().getId(); }

  private static long glThreadId;
  public static void setGlThread() { glThreadId = Thread.currentThread().getId(); }
  public static boolean isGlThread() { return glThreadId == Thread.currentThread().getId(); }

  public static boolean debuggable;

  public static ExecutorService threadPool;
  public static Context context;
  public static Content content;
  public static IRenderer renderer;
  public static IGlThread glThread;
  public static Resources resources;
  public static CollisionManager collisions;
  public static Factory factory;
  public static DataManager data;
}
