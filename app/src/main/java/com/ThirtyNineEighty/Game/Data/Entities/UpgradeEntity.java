package com.ThirtyNineEighty.Game.Data.Entities;

import java.io.Serializable;

public class UpgradeEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public boolean opened;

  public UpgradeEntity(boolean opened)
  {
    this.opened = opened;
  }
}
