package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.System.IView;

public interface IRenderable
  extends IView
{
  boolean isVisible();
  void setVisible(boolean value);

  int getShaderId();
  void draw(RendererContext context);
}
