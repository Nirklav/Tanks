package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.System.IBindable;

import java.util.List;

public interface IWorld
  extends IBindable
{
  // view support
  void fillRenderable(List<I3DRenderable> renderables);
  void setCamera(Camera camera);
  void setLight(Vector3 lightPosition);

  // world
  EngineObject getPlayer();

  void fillObjects(List<EngineObject> objects);

  void add(EngineObject engineObject);
  void add(EngineObject engineObject, Object args);
  void remove(EngineObject engineObject);
}
