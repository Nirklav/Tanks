package com.ThirtyNineEighty.Game.Map.Descriptions;

import java.io.Serializable;

@SuppressWarnings("UnusedDeclaration") // fields set by serialization
public class MapDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public float size;
  public MapObject[] objects;
  public MapState player;
  public String[] subprograms;

  public String[] openingMaps;
  public String[] openingTanks;
  public String[] openingUpgrades;
}
