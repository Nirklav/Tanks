package com.ThirtyNineEighty.Base.Renderable.Subprograms;

import com.ThirtyNineEighty.Base.Renderable.IRenderable;
import com.ThirtyNineEighty.Base.Subprogram;

public class DelayedRenderableSubprogram
  extends Subprogram
{
  private final IRenderable renderable;
  private final int timeout;
  private boolean delayed;

  public DelayedRenderableSubprogram(IRenderable renderable, int timeout)
  {
    this.renderable = renderable;
    this.timeout = timeout;
  }

  @Override
  protected void onUpdate()
  {
    if (!delayed)
    {
      delayed = true;
      delay(timeout);
      return;
    }

    renderable.setVisible(false);
    unbind();
  }
}
