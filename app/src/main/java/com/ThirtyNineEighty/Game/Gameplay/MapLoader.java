package com.ThirtyNineEighty.Game.Gameplay;

import android.content.res.AssetManager;

import com.ThirtyNineEighty.Game.Gameplay.Subprograms.BotSubprogram;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MapLoader
{
  private HashMap<String, Class> objectBindings;
  private HashMap<String, Class> subprogramBindings;
  private ArrayList<String> maps;

  public void initialize()
  {
    objectBindings = new HashMap<>();
    objectBindings.put("tank", Tank.class);
    objectBindings.put("botTank", Tank.class);
    objectBindings.put("land", Land.class);
    objectBindings.put("bullet", Bullet.class);
    objectBindings.put("building", Decor.class);

    subprogramBindings = new HashMap<>();
    subprogramBindings.put("bot", BotSubprogram.class);

    maps = loadMapNames();
  }

  public MapDescription load(String name)
  {
    IWorld world = GameContext.content.getWorld();
    MapDescription map = Serializer.Deserialize(String.format("Maps/%s.map", name));

    for (MapDescription.MapObject obj : map.objects)
    {
      IEngineObject object = createObject(obj.name);
      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      if (obj.subprograms != null)
        for (String subprogramName : obj.subprograms)
        {
          ISubprogram subprogram = createSubprogram(subprogramName, object);
          object.bindProgram(subprogram);
        }

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

  // TODO: for now only for GameObject
  private ISubprogram createSubprogram(String name, IEngineObject bindObject)
  {
    try
    {
      Class<?> subprogramClass = subprogramBindings.get(name);
      Constructor<?> constructor = subprogramClass.getConstructor(GameObject.class);
      return (ISubprogram) constructor.newInstance((GameObject) bindObject);
    }
    catch (Exception e)
    {
      throw new RuntimeException(String.format("Can't create subprogram with %s name", name), e);
    }
  }

  private ArrayList<String> loadMapNames()
  {
    try
    {
      ArrayList<String> maps = new ArrayList<>();

      AssetManager manager = GameContext.activity.getAssets();
      String[] files = manager.list("Maps");

      for (String fileName : files)
      {
        int pos = fileName.lastIndexOf('.');
        maps.add(pos > 0 ? fileName.substring(0, pos) : fileName);
      }

      return maps;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
