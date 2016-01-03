package com.ThirtyNineEighty.Base.Data.Entities;

import com.ThirtyNineEighty.Base.Worlds.IWorld;

import java.io.Serializable;

public class SavedWorldEntity
  implements Serializable
{
  public IWorld world;

  public SavedWorldEntity(IWorld world)
  {
    this.world = world;
  }
}
