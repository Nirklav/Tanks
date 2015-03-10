package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

public interface IEngineObject
{
  // callbacks
  void onMoved(float length);
  void onMoved(Vector3 vector, float length);
  void onRotates(Vector3 angles);

  void onCollide(IEngineObject object);

  void onRemoved();

  // properties
  Vector3 getAngles();
  Vector3 getPosition();

  void setAngles(Vector3 value);
  void setPosition(Vector3 value);

  I3DRenderable getRenderable();
  ICollidable getCollidable();
}
