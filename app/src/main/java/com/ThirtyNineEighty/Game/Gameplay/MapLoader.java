package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MapLoader
{
  private HashMap<String, Class> objectBindings;
  private ArrayList<String> maps;

  public void initialize()
  {
    objectBindings = new HashMap<String, Class>();
    objectBindings.put("tank", Tank.class);
    objectBindings.put("land", Land.class);
    objectBindings.put("bullet", Bullet.class);
    objectBindings.put("building", Decor.class);

    maps = loadMapNames();
  }

  public MapDescription load(String name)
  {
    IContent content = GameContext.getContent();
    IWorld world = content.getWorld();
    MapDescription map = Serializer.Deserialize(String.format("Maps/%s.map", name));

    for(MapDescription.MapObject obj : map.objects)
    {
      IEngineObject object = createObject(obj.name);
      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      world.add(object);
    }

    return map;
  }

  public List<String> getMaps() { return maps; }

  private IEngineObject createObject(String name)
  {
    try
    {
      Class<?> objectClass = objectBindings.get(name);
      Constructor<?> constructor = objectClass.getConstructor(String.class);
      return (IEngineObject) constructor.newInstance(name);
    }
    catch (Exception e)
    {
      throw new RuntimeException(String.format("Can't create object with %s name", name), e);
    }
  }

  private ArrayList<String> loadMapNames()
  {
    // TODO: load map names from dir
    ArrayList<String> maps = new ArrayList<String>();
    maps.add("standard");
    return maps;
  }
}
