package com.ThirtyNineEighty.Game.Data.Entities;

import java.io.Serializable;

public class TankEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public boolean opened;

  public TankEntity(boolean opened)
  {
    this.opened = opened;
  }
}
