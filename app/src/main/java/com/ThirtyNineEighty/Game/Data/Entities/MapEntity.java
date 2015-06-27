package com.ThirtyNineEighty.Game.Data.Entities;

import java.io.Serializable;

public class MapEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public boolean opened;

  public MapEntity(boolean opened)
  {
    this.opened = opened;
  }
}
