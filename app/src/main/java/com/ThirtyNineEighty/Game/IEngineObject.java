package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.ISubprogram;

public interface IEngineObject
{
  // callbacks
  void onMoved(float length);
  void onMoved(float length, Vector3 vector);
  void onRotates(Vector3 angles);
  void onCollide(IEngineObject object);

  // life cycle
  void dispose();

  void enable();
  void disable();

  // subprograms
  void bindProgram(ISubprogram subprogram);
  void unbindProgram(ISubprogram subprogram);

  // properties
  Vector3 getAngles();
  Vector3 getPosition();

  ICollidable getCollidable();
  I3DRenderable[] getRenderables();

  // position
  void setAngles(Vector3 value);
  void setPosition(Vector3 value);

  void setGlobalRenderablePosition();
  void setGlobalCollidablePosition();
}
