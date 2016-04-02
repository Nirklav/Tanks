package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.GameContext;

public class Gesture
  extends Control
{
  private final Object syncObject = new Object();

  private Runnable gestureListener;

  private Vector2 startPoint;
  private Vector2 endPoint;

  public Gesture()
  {
    startPoint = Vector2.getInstance();
    endPoint = Vector2.getInstance();
  }

  // called from update thread
  public Vector2 get()
  {
    synchronized (syncObject)
    {
      return startPoint.getSubtract(endPoint);
    }
  }

  @Override
  protected void onDown(float x, float y)
  {
    synchronized (syncObject)
    {
      startPoint.setFrom(0, 0);
      endPoint.setFrom(x, y);
    }
  }

  @Override
  protected void onMove(float x, float y)
  {
    synchronized (syncObject)
    {
      startPoint.setFrom(endPoint);
      endPoint.setFrom(x, y);
    }

    GameContext.content.postEvent(gestureListener);
  }

  @Override
  protected void onUp(float x, float y)
  {
    synchronized (syncObject)
    {
      endPoint.setFrom(x, y);
    }

    GameContext.content.postEvent(gestureListener);
  }

  public void setGestureListener(Runnable listener)
  {
    gestureListener = listener;
  }

  @Override
  protected boolean canProcess(float x, float y)
  {
    return true;
  }
}
