package com.ThirtyNineEighty.Game.Data.Entities;

import java.io.Serializable;

public class CampaignEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public String currentMap;

  public CampaignEntity(String currentMap)
  {
    this.currentMap = currentMap;
  }
}
