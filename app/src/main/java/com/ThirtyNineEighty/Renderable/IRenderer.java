package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.Providers.IDataProvider;

public interface IRenderer
{
  void add(IRenderable renderable);
  void remove(IRenderable renderable);

  void setCameraProvider(IDataProvider<Camera> provider);
  void setLightProvider(IDataProvider<Light> provider);
}
