package com.ThirtyNineEighty.Base.Renderable;

public interface IRenderable
  extends IView
{
  boolean isVisible();
  void setVisible(boolean value);

  int getShaderId();
  void draw(RendererContext context);
}
