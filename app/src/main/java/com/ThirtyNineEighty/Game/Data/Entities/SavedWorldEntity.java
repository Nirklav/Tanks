package com.ThirtyNineEighty.Game.Data.Entities;

import com.ThirtyNineEighty.Game.Worlds.IWorld;

import java.io.Serializable;

public class SavedWorldEntity
  implements Serializable
{
  public String mapName;
  public IWorld world;

  public SavedWorldEntity(String mapName, IWorld world)
  {
    this.mapName = mapName;
    this.world = world;
  }
}
