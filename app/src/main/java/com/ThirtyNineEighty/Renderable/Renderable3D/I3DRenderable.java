package com.ThirtyNineEighty.Renderable.Renderable3D;

import com.ThirtyNineEighty.Helpers.Vector3;

public interface I3DRenderable
{
  void draw(float[] modelProjectionViewMatrix, Vector3 lightPosition);

  void setGlobal(Vector3 position, Vector3 angles);
  void setLocal(Vector3 position, Vector3 angles);
}
