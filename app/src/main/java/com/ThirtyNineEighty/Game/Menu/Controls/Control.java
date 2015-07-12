package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public abstract class Control
  implements IControl
{
  private int pointerId;
  private Runnable clickListener;
  private ArrayList<I2DRenderable> renderables;

  protected Control()
  {
    pointerId = -1;
    renderables = new ArrayList<>();
  }

  protected void addRenderable(I2DRenderable renderable)
  {
    renderables.add(renderable);
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    for (I2DRenderable renderable : renderables)
      renderable.draw(orthoViewMatrix);
  }

  @Override
  public void setVisible(boolean value)
  {
    for (I2DRenderable renderable : renderables)
      renderable.setVisible(value);
  }

  @Override
  public boolean isVisible()
  {
    if (renderables.size() == 0)
      return false;
    I2DRenderable first = renderables.get(0);
    return first.isVisible();
  }

  @Override
  public final void processDown(int pointerId, float x, float y)
  {
    if (canProcess(x, y) && this.pointerId == -1)
    {
      this.pointerId = pointerId;
      onDown(x, y);
    }
  }

  @Override
  public final void processMove(int pointerId, float x, float y)
  {
    if (this.pointerId == pointerId)
      onMove(x, y);
  }

  @Override
  public final void processUp(int pointerId, float x, float y)
  {
    if (this.pointerId == pointerId)
    {
      this.pointerId = -1;

      onUp(x, y);

      if (clickListener != null)
      {
        // execute on Update thread
        GameContext.content.postEvent(clickListener);
      }
    }
  }

  protected void onDown(float x, float y) { }
  protected void onMove(float x, float y) { }
  protected void onUp(float x, float y) { }

  protected abstract boolean canProcess(float x, float y);

  public void setClickListener(Runnable listener) { clickListener = listener; }
}
