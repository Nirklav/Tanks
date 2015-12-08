package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.IEngineObject;

public interface IRenderable
  extends IEngineObject
{
  void setBindable(IBindable bindable);

  boolean isVisible();
  void setVisible(boolean value);

  int getShaderId();
  void draw(RendererContext context);
}
