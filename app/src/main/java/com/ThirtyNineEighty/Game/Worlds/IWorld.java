package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

import java.util.Collection;
import java.util.List;

public interface IWorld
{
  // life cycle
  boolean isInitialized();

  void initialize(Object args);
  void uninitialize();

  void enable();
  void disable();

  // view support
  void fillRenderable(List<I3DRenderable> renderables);
  void setViewMatrix(float[] viewMatrix);

  // world
  IEngineObject getPlayer();
  void fillObjects(List<IEngineObject> objects);

  void add(IEngineObject engineObject);
  void remove(IEngineObject engineObject);
}
