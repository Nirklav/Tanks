package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Renderable.IRenderable;
import com.ThirtyNineEighty.System.Subprogram;

public class TimedSubprogram
  extends Subprogram
{
  private boolean isFirst = true;
  private int delayMs;
  private IRenderable renderable;

  public TimedSubprogram(IRenderable renderable, int delayMs)
  {
    this.renderable = renderable;
    this.delayMs = delayMs;
  }

  @Override
  protected void onUpdate()
  {
    if (isFirst)
    {
      isFirst = false;
      bindable.bind(renderable);
      delay(delayMs);
      return;
    }
    bindable.unbind(renderable);
    unbind();
  }
}
