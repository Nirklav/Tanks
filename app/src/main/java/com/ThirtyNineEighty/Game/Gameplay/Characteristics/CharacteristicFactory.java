package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

import com.ThirtyNineEighty.Game.EngineObjectDescription;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

// Characteristic file format:

// int - visualModels count
// {
//   20 chars - VisualModelName (in folder Models with extension raw)
//   20 chars - TextureName (in folder Textures with extension png)
// }

// 20 chars - PhysicalModelName (in folder Models with extension ph)
// float - Health
// float - Speed
// float - Damage
// float - RotationSpeed

public class CharacteristicFactory
{
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

      int visualModelsCount = buffer.getInt();
      EngineObjectDescription initializer = new EngineObjectDescription();

      for (int i = 0; i < visualModelsCount; i++)
      {
        String visualModelName = readString(buffer, 20);
        String textureName = readString(buffer, 20);

        initializer.VisualModels.add(new EngineObjectDescription.VisualModelDescription(visualModelName, textureName));
      }

      String phModelName = readString(buffer, 20);
      initializer.PhysicalModel = new EngineObjectDescription.PhysicalModelDescription(phModelName);

      c = new Characteristic(initializer);
      c.setHealth(buffer.getFloat());
      c.setSpeed(buffer.getFloat());
      c.setDamage(buffer.getFloat());
      c.setRotationSpeed(buffer.getFloat());

      cache.put(type, c);
      return new Characteristic(c);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
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
