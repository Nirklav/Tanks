package com.ThirtyNineEighty.Renderable;

public interface IRenderable
{
  int getShaderId();

  boolean isVisible();
  void setVisible(boolean value);

  void draw(RendererContext context);
}
