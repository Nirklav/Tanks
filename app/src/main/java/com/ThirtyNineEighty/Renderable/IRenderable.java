package com.ThirtyNineEighty.Renderable;

public interface IRenderable
{
  int getShaderId();
  boolean isVisible();

  void draw(RendererContext context);
}
