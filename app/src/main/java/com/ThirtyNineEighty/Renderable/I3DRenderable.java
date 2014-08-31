package com.ThirtyNineEighty.Renderable;

public interface I3DRenderable
{
  void draw(float[] modelProjectionViewMatrix, float[] light);

  float[] getPosition();
  float getXAngle();
  float getYAngle();
  float getZAngle();
}
