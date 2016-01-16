package com.ThirtyNineEighty.Base.Renderable;

public interface IRenderable
  extends IView
{
  boolean isVisible();
  int getShaderId();
  void draw(RendererContext context);
}
