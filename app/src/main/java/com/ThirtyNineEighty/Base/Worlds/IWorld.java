package com.ThirtyNineEighty.Base.Worlds;

import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.IBindable;

import java.util.List;

public interface IWorld
  extends IBindable
{
  // player
  WorldObject<?, ?> getPlayer();

  // world
  IMap getMap();

  void getObjects(List<WorldObject<?, ?>> objects);
  void add(WorldObject<?, ?> worldObject);
  void remove(WorldObject<?, ?> worldObject);

  // save
  boolean needSave();
}
