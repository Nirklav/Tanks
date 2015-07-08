package com.ThirtyNineEighty.Renderable.Renderable2D;

public interface I2DRenderable
{
  void draw(float[] orthoViewMatrix);

  void setVisible(boolean value);
  boolean isVisible();
}
