package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.System.GameContext;

public class Gesture
  extends Control
{
  private Runnable gestureListener;

  private Vector2 startPoint;
  private Vector2 endPoint;

  public Gesture()
  {
    startPoint = Vector.getInstance(2);
    endPoint = Vector.getInstance(2);
  }

  public Vector2 get() { return startPoint.getSubtract(endPoint); }

  @Override
  protected void onDown(float x, float y)
  {
    startPoint.setFrom(0, 0);
    endPoint.setFrom(x, y);
  }

  @Override
  protected void onMove(float x, float y)
  {
    startPoint.setFrom(endPoint);
    endPoint.setFrom(x, y);

    GameContext.content.postEvent(gestureListener);
  }

  @Override
  protected void onUp(float x, float y)
  {
    endPoint.setFrom(x, y);

    GameContext.content.postEvent(gestureListener);
  }

  public void setGestureListener(Runnable listener) { gestureListener = listener; }
  @Override protected boolean canProcess(float x, float y) { return true; }
  @Override public void draw(float[] orthoViewMatrix) { }
}
