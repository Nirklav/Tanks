package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

public interface IGameObject
{
  // manage
  void move(float length);
  void rotate(float angleX, float angleY, float angleZ);

  // callbacks
  void onMoved(float length);
  void onMoved(Vector3 vector, float length);
  void onRotates(float angleX, float angleY, float angleZ);

  // properties
  long getId();

  float getRadius();

  float getAngleX();
  float getAngleY();
  float getAngleZ();

  Vector3 getPosition();

  I3DRenderable getRenderable();
  ICollidable getCollidable();
}
