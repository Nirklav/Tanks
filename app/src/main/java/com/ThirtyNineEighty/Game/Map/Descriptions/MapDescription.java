package com.ThirtyNineEighty.Game.Map.Descriptions;

import com.ThirtyNineEighty.Base.Resources.Entities.Resource;

@SuppressWarnings("UnusedDeclaration") // fields set by serialization
public class MapDescription
  extends Resource
{
  private static final long serialVersionUID = 1L;

  public float size;
  public MapObject[] objects;
  public MapState player;
  public String[] subprograms;

  public String[] openingMaps;
  public String[] openingTanks;
  public String[] openingUpgrades;

  public boolean openedOnStart;

  public MapDescription(String name)
  {
    super(name);
  }
}
