package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.System.GameContext;

public abstract class Renderable
  extends View
  implements IRenderable
{
  private static final long serialVersionUID = 1L;

  private boolean visible = true;

  @Override
  public boolean isVisible()
  {
    return visible;
  }

  @Override
  public void setVisible(boolean value)
  {
    visible = value;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    GameContext.renderer.add(this);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.remove(this);

    super.uninitialize();
  }

  @Override
  public void enable()
  {
    IDataProvider provider = getProvider();
    if (provider != null)
      provider.set();

    super.enable();
  }
}
