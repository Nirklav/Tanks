package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Game.Menu.Controls.IControl;
import com.ThirtyNineEighty.System.Bindable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public abstract class BaseMenu
  extends Bindable
  implements IMenu
{
  private final Object syncObject;
  private final ArrayList<IControl> controls;
  private final ArrayList<IControl> controlsCopy;

  protected BaseMenu()
  {
    syncObject = new Object();
    controls = new ArrayList<>();
    controlsCopy = new ArrayList<>();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    ArrayList<IControl> controlsCopy;
    synchronized (controls)
    {
      controlsCopy = new ArrayList<>(controls);
      controls.clear();
    }

    for (IControl object : controlsCopy)
      object.uninitialize();
  }

  protected void addControl(IControl control)
  {
    if (!isInitialized())
      throw new IllegalStateException("menu not initialized");

    synchronized (syncObject)
    {
      controls.add(control);
    }
    control.initialize();
  }

  protected void removeControl(IControl control)
  {
    synchronized (syncObject)
    {
      controls.remove(control);
    }
    control.uninitialize();
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
