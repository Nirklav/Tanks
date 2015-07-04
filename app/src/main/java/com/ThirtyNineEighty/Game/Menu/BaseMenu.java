package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Game.Menu.Controls.IControl;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.System.Bindable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMenu
  extends Bindable
  implements IMenu
{
  private final Object syncObject;
  private final ArrayList<IControl> controls;
  private final ArrayList<IControl> controlsCopy;
  private final ArrayList<I2DRenderable> renderables;

  protected BaseMenu()
  {
    syncObject = new Object();
    controls = new ArrayList<>();
    controlsCopy = new ArrayList<>();
    renderables = new ArrayList<>();
  }

  protected void addControl(IControl control)
  {
    synchronized (syncObject)
    {
      controls.add(control);
    }
  }

  protected void removeControl(IControl control)
  {
    synchronized (syncObject)
    {
      controls.remove(control);
    }
  }

  protected void addRenderable(I2DRenderable control)
  {
    synchronized (syncObject)
    {
      renderables.add(control);
    }
  }

  protected void removeRenderable(I2DRenderable control)
  {
    synchronized (syncObject)
    {
      renderables.remove(control);
    }
  }

  @Override
  public final void fillRenderable(List<I2DRenderable> filled)
  {
    synchronized (syncObject)
    {
      for (I2DRenderable renderable : controls)
        filled.add(renderable);

      for (I2DRenderable renderable : renderables)
        filled.add(renderable);
    }
  }

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

    synchronized (syncObject)
    {
      controlsCopy.clear();
      controlsCopy.addAll(controls);
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
