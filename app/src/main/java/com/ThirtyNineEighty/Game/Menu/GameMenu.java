package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

import java.util.Collection;

public class GameMenu
  implements IMenu
{
  private int forwardID;
  private boolean forwardState;

  private int leftID;
  private boolean leftState;

  private int rightID;
  private boolean rightState;

  @Override
  public Collection<I2DRenderable> getControls()
  {
    return null;
  }

  @Override
  public boolean processEvent(MotionEvent event, float width, float height)
  {
    int action = event.getActionMasked();
    int pointerIndex = event.getActionIndex();

    float leftBorder = (1.0f / 3.0f) * width;
    float rightBorder = (2.0f / 3.0f) * width;

    float x = event.getX(pointerIndex);
    float y = event.getY(pointerIndex);
    int id = event.getPointerId(pointerIndex);

    switch (action)
    {
    case MotionEvent.ACTION_DOWN:
    case MotionEvent.ACTION_POINTER_DOWN:
      if (x > leftBorder && x < rightBorder)
      {
        forwardState = true;
        forwardID = id;
      }

      if (x <= leftBorder)
      {
        leftState = true;
        leftID = id;
      }

      if (x >= rightBorder)
      {
        rightState = true;
        rightID = id;
      }
      break;

    case MotionEvent.ACTION_UP:
    case MotionEvent.ACTION_POINTER_UP:
    case MotionEvent.ACTION_CANCEL:
      if (id == forwardID)
        forwardState = false;

      if (id == leftID)
        leftState = false;

      if (id == rightID)
        rightState = false;
    }

    return true;
  }

  public boolean getForwardState()
  {
    return forwardState;
  }

  public boolean getLeftState()
  {
    return leftState;
  }

  public boolean getRightState()
  {
    return rightState;
  }
}
