package com.ThirtyNineEighty.Base.Renderable;

import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.Common.Camera;
import com.ThirtyNineEighty.Base.Renderable.Common.Light;

public interface IRenderer
{
  void add(IRenderable renderable);
  void remove(IRenderable renderable);

  void setCameraProvider(IDataProvider<Camera.Data> provider);
  void setLightProvider(IDataProvider<Light.Data> provider);
}
