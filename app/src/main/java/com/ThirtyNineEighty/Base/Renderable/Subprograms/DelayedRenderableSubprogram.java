package com.ThirtyNineEighty.Base.Renderable.Subprograms;

import com.ThirtyNineEighty.Base.Providers.RenderableDataProvider;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;

public class DelayedRenderableSubprogram
  extends Subprogram
{
  private final RenderableDataProvider provider;
  private final int timeout;
  private boolean delayed;

  public DelayedRenderableSubprogram(RenderableDataProvider provider, int timeout)
  {
    this.provider = provider;
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

    provider.setVisible(false);
    unbind();
  }
}
