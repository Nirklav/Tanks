package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Renderable.RendererContext;

public interface IRenderable
  extends IEngineObject
{
  int getShaderId();

  boolean isVisible();
  void setVisible(boolean value);

  void draw(RendererContext context);

  void setBindable(IBindable bindable);
}
