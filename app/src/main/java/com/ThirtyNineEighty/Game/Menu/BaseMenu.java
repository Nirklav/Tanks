package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Game.Menu.Controls.IControl;
import com.ThirtyNineEighty.Renderable.IRenderable;
import com.ThirtyNineEighty.Renderable.Renderer;
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
  private final ArrayList<IRenderable> renderables;

  protected BaseMenu()
  {
    syncObject = new Object();
    controls = new ArrayList<>();
    controlsCopy = new ArrayList<>();
    renderables = new ArrayList<>();
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

    ArrayList<IRenderable> renderablesCopy;
    synchronized (renderables)
    {
      renderablesCopy = new ArrayList<>(renderables);
      renderables.clear();
    }

    for (IRenderable renderable : renderablesCopy)
      Renderer.remove(renderable);
  }

  protected void addControl(IControl control)
  {
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

  protected void addRenderable(IRenderable renderable)
  {
    synchronized (syncObject)
    {
      renderables.add(renderable);
    }
    Renderer.add(renderable);
  }

  protected void removeRenderable(IRenderable renderable)
  {
    synchronized (syncObject)
    {
      renderables.remove(renderable);
    }
    Renderer.remove(renderable);
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
