package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

public interface IEngineObject
{
  // manage
  void move(float length);
  void rotate(Vector3 angles);

  // callbacks
  void onMoved(float length);
  void onMoved(Vector3 vector, float length);
  void onRotates(Vector3 angles);

  // properties
  Vector3 getAngles();
  Vector3 getPosition();

  I3DRenderable getRenderable();
  ICollidable getCollidable();
}
