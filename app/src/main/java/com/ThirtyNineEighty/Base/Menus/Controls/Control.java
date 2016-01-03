package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Bindable;
import com.ThirtyNineEighty.Base.GameContext;

public abstract class Control
  extends Bindable
  implements IControl
{
  private int pointerId;
  private Runnable clickListener;

  protected Control()
  {
    pointerId = -1;
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
