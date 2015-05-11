package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.Camera;

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
  void setCamera(Camera camera);
  void setLight(Vector3 lightPosition);

  // world
  IEngineObject getPlayer();
  void fillObjects(List<IEngineObject> objects);

  void add(IEngineObject engineObject);
  void remove(IEngineObject engineObject);
}
