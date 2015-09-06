package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Renderable.RendererContext;

public interface IRenderable
  extends IEngineObject
{
  void setBindable(IBindable bindable);

  boolean isVisible();
  void setVisible(boolean value);

  int getShaderId();
  void draw(RendererContext context);
}
