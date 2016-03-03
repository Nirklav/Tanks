package com.ThirtyNineEighty.Base.Menus;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Base.Menus.Controls.IControl;
import com.ThirtyNineEighty.Base.BindableHost;
import com.ThirtyNineEighty.Base.Renderable.Renderer;

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

    float widthRatio = Renderer.EtalonWidth / Renderer.getWidth();
    float heightRatio = Renderer.EtalonHeight / Renderer.getHeight();

    float x = event.getX(pointerIndex) - Renderer.getWidth() / 2;
    float y = - (event.getY(pointerIndex) - Renderer.getHeight() / 2);

    x *= widthRatio;
    y *= heightRatio;

    int id = event.getPointerId(pointerIndex);

    synchronized (objects)
    {
      controlsCopy.clear();

      for (IControl control : objects)
        controlsCopy.add(control);
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
