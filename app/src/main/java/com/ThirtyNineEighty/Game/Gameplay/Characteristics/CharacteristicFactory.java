package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

import com.ThirtyNineEighty.Helpers.Serializer;

import java.util.HashMap;

public class CharacteristicFactory
{
  public static final String TANK = "tank";
  public static final String BULLET = "bullet";
  public static final String LAND = "land";

  private static final HashMap<String, Characteristic> cache = new HashMap<String, Characteristic>();

  public static Characteristic get(String type)
  {
    Characteristic c = cache.get(type);
    if (c != null)
      return new Characteristic(c);

    c = Serializer.Deserialize(String.format("Characteristics/%s.ch", type));
    cache.put(type, c);
    return c;
  }
}
