package com.ThirtyNineEighty.Game.Characteristics;

import com.ThirtyNineEighty.Helpers.Serializer;

import java.util.HashMap;

public class CharacteristicFactory
{
  public static final String Tank = "tank";
  public static final String SpeedTank = "speedTank";
  public static final String Bullet = "bullet";

  private static final HashMap<String, Characteristic> cache = new HashMap<>();

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
