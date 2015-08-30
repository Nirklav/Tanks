package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Game.Menu.Controls.IControl;
import com.ThirtyNineEighty.System.BindableHost;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public abstract class BaseMenu
  extends BindableHost<IControl>
  implements IMenu
{
  private final ArrayList<IControl> controlsCopy = new ArrayList<>();

  @Override
  public final void processEvent(MotionEvent event)
  {
    int action = event.getActionMasked();
    int pointerIndex = event.getActionIndex();

    float widthRatio = GameContext.EtalonWidth / GameContext.getWidth();
    float heightRatio = GameContext.EtalonHeight / GameContext.getHeight();

    float x = event.getX(pointerIndex) - GameContext.getWidth() / 2;
    float y = - (event.getY(pointerIndex) - GameContext.getHeight() / 2);

    x *= widthRatio;
    y *= heightRatio;

    int id = event.getPointerId(pointerIndex);

    synchronized (objects)
    {
      controlsCopy.clear();
      controlsCopy.addAll(objects);
    }

    switch (action)
    {
    case MotionEvent.ACTION_DOWN:
    case MotionEvent.ACTION_POINTER_DOWN:
      for (IControl control : controlsCopy)
        control.processDown(id, x, y);
      break;

    case MotionEvent.ACTION_MOVE:
      for (IControl control : controlsCopy)
        control.processMove(id, x, y);
      break;

    case MotionEvent.ACTION_UP:
    case MotionEvent.ACTION_POINTER_UP:
    case MotionEvent.ACTION_CANCEL:
      for (IControl control : controlsCopy)
        control.processUp(id, x, y);
      break;
    }
  }
}
