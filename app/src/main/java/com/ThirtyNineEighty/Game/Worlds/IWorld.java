package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.System.IBindable;

import java.util.List;

public interface IWorld
  extends IBindable
{
  // player
  WorldObject<?, ?> getPlayer();

  // world
  void getObjects(List<WorldObject<?, ?>> objects);
  void add(WorldObject<?, ?> worldObject);
  void remove(WorldObject<?, ?> worldObject);

  // save
  boolean needSave();
}
