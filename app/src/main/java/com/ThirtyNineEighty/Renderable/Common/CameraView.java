package com.ThirtyNineEighty.Renderable.Common;

import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.View;
import com.ThirtyNineEighty.System.GameContext;

public class CameraView
  extends View
{
  private IDataProvider<Camera> provider;

  public CameraView(IDataProvider<Camera> provider)
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

    GameContext.renderer.setCameraProvider(provider);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.setCameraProvider(null);

    super.uninitialize();
  }
}
