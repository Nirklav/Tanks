package com.ThirtyNineEighty.Renderable.Renderable3D;

import com.ThirtyNineEighty.Helpers.Vector3;

public interface I3DRenderable
{
  void draw(float[] modelProjectionViewMatrix, float[] light);

  Vector3 getPosition();
  float getXAngle();
  float getYAngle();
  float getZAngle();
}
