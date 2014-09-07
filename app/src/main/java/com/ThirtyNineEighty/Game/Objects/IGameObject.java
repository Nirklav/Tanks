package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.I3DRenderable;

public interface IGameObject
{
  // manage
  void move(float length);
  void rotate(float angleX, float angleY, float angleZ);

  // callbacks
  void onMoved(float length);
  void onRotates(float angleX, float angleY, float angleZ);

  // properties
  long getId();

  Vector3 getPosition();
  float getAngleX();
  float getAngleY();
  float getAngleZ();
  I3DRenderable getVisualModel();
  IPhysicalObject getPhysicalModel();
}
