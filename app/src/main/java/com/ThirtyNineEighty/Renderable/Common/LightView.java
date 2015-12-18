package com.ThirtyNineEighty.Renderable.Common;

import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.Light;
import com.ThirtyNineEighty.Renderable.View;
import com.ThirtyNineEighty.System.GameContext;

public class LightView
  extends View
{
  private IDataProvider<Light> provider;

  public LightView(IDataProvider<Light> provider)
  {
    this.provider = provider;
  }

  @Override
  public IDataProvider getProvider()
  {
    return provider;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    GameContext.renderer.setLightProvider(provider);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.setLightProvider(null);

    super.uninitialize();
  }
}
