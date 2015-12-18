package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.Common.Camera;
import com.ThirtyNineEighty.Renderable.Common.Light;

public interface IRenderer
{
  void add(IRenderable renderable);
  void remove(IRenderable renderable);

  void setCameraProvider(IDataProvider<Camera.Data> provider);
  void setLightProvider(IDataProvider<Light.Data> provider);
}
