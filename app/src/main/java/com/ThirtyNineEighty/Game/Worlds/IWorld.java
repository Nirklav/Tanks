package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.Light;
import com.ThirtyNineEighty.System.IBindable;

import java.util.List;

public interface IWorld
  extends IBindable
{
  // view support
  void setCamera(Camera camera);
  void setLight(Light light);

  // player
  WorldObject<?, ?> getPlayer();

  // world
  void getObjects(List<WorldObject<?, ?>> objects);
  void add(WorldObject<?, ?> worldObject);
  void remove(WorldObject<?, ?> worldObject);

  // save
  boolean needSave();
}
