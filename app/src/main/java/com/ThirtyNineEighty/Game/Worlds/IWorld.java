package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

import java.util.Collection;
import java.util.List;

public interface IWorld
{
  void initialize(Object args);
  void uninitialize();

  void fillRenderable(List<I3DRenderable> renderables);
  void setViewMatrix(float[] viewMatrix);

  Collection<IEngineObject> getObjects();
  IEngineObject getPlayer();

  void add(IEngineObject engineObject);
  void remove(IEngineObject engineObject);
}
