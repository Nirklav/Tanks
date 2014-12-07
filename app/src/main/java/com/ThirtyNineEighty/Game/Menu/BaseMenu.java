package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMenu
  implements IMenu
{
  private ArrayList<IEventProcessor> processors;

  protected BaseMenu()
  {
    processors = new ArrayList<IEventProcessor>();
  }

  protected void addEventProcessor(IEventProcessor processor)
  {
    processors.add(processor);
  }

  protected void removeEventProcessor(IEventProcessor processor)
  {
    processors.remove(processor);
  }

  @Override
  public abstract void fillRenderable(List<I2DRenderable> renderables);

  @Override
  public final boolean processEvent(MotionEvent event)
  {
    int action = event.getActionMasked();
    int pointerIndex = event.getActionIndex();

    float x = event.getX(pointerIndex) - GameContext.EtalonWidth / 2;
    float y = - (event.getY(pointerIndex) - GameContext.EtalonHeight / 2);
    int id = event.getPointerId(pointerIndex);

    switch (action)
    {
    case MotionEvent.ACTION_DOWN:
    case MotionEvent.ACTION_POINTER_DOWN:
      for(IEventProcessor processor : processors)
        processor.processDown(id, x, y);
      break;

    case MotionEvent.ACTION_MOVE:
      for(IEventProcessor processor : processors)
        processor.processMove(id, x, y);
      break;

    case MotionEvent.ACTION_UP:
    case MotionEvent.ACTION_POINTER_UP:
    case MotionEvent.ACTION_CANCEL:
      for(IEventProcessor processor : processors)
        processor.processUp(id, x, y);
      break;
    }

    return true;
  }
}
