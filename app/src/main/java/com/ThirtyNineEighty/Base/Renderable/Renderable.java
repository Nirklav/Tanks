package com.ThirtyNineEighty.Base.Renderable;

import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.Serializable;

public abstract class Renderable
  extends View
  implements IRenderable
{
  protected static final String ParticleTexture = "particle";
  protected static final String FontTexture = "SimpleFont";

  private static final long serialVersionUID = 1L;

  @Override
  public void initialize()
  {
    super.initialize();

    IDataProvider provider = getProvider();
    if (provider != null)
      provider.set();

    GameContext.renderer.add(this);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.remove(this);

    super.uninitialize();
  }

  public static class Data
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public boolean visible;
  }
}
