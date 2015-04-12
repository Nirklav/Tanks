package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public final class MapLoader
{
  private static HashMap<String, Class> objectBindings;
  static
  {
    objectBindings = new HashMap<String, Class>();
    objectBindings.put("tank", Tank.class);
    objectBindings.put("land", Decor.class);
    objectBindings.put("bullet", Bullet.class);
  }

  public static Map.Player load(String name)
  {
    IContent content = GameContext.getContent();
    IWorld world = content.getWorld();
    Map map = Serializer.Deserialize(String.format("Maps/%s.map", name));

    for(Map.MapObject obj : map.objects)
    {
      IEngineObject object = createObject(obj.name);
      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      world.add(object);
    }

    return map.player;
  }

  private static IEngineObject createObject(String name)
  {
    try
    {
      Class<?> objectClass = objectBindings.get(name);
      Constructor<?> constructor = objectClass.getConstructor(String.class);
      return (IEngineObject) constructor.newInstance(name);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
