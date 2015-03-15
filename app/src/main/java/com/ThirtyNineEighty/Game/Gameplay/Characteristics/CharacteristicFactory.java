package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

import android.util.Log;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

// Characteristic file format:
// 20 chars - VisualModelName (in folder Models with extension raw)
// 20 chars - PhysicalModelName (in folder Models with extension ph)
// 20 chars - TextureName (in folder Textures with extension png)
// float - Health
// float - Speed
// float - Damage
// float - RotationSpeed

public class CharacteristicFactory
{
  private static final String Tag = "CharacteristicFactory";

  public static final String TANK = "tank";
  public static final String BULLET = "bullet";

  private static final HashMap<String, Characteristic> cache = new HashMap<String, Characteristic>();

  public static Characteristic get(String type)
  {
    String fileName = String.format("Characteristics/%s.ch", type);
    Characteristic c = cache.get(type);
    if (c != null)
      return new Characteristic(c);

    try
    {
      InputStream stream = GameContext.getAppContext().getAssets().open(fileName);

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer buffer = ByteBuffer.wrap(data);

      String visualModelName = readString(buffer, 20);
      String phModelName = readString(buffer, 20);
      String textureName = readString(buffer, 20);

      c = new Characteristic(visualModelName, phModelName, textureName);
      c.setHealth(buffer.getFloat());
      c.setSpeed(buffer.getFloat());
      c.setDamage(buffer.getFloat());
      c.setRotationSpeed(buffer.getFloat());

      cache.put(type, c);
      return new Characteristic(c);
    }
    catch (IOException e)
    {
      Log.e(Tag, "get method error", e);
      return null;
    }
  }

  private static String readString(ByteBuffer buffer, int length)
  {
    StringBuilder builder = new StringBuilder(length);

    for(int i = 0; i < length; i++)
      builder.append(buffer.getChar());

    return builder.toString().trim();
  }
}
