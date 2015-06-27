package com.ThirtyNineEighty.Game.Map;

import android.content.res.AssetManager;

import com.ThirtyNineEighty.Game.Objects.AidKit;
import com.ThirtyNineEighty.Game.Objects.Bullet;
import com.ThirtyNineEighty.Game.Objects.Decor;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Subprograms.BotSubprogram;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MapManager
{
  private HashMap<String, Class> objectBindings;
  private HashMap<String, Class> subprogramBindings;
  private ArrayList<String> maps;

  private MapDescription description;
  private Map map;

  public void initialize()
  {
    objectBindings = new HashMap<>();
    objectBindings.put("tank", Tank.class);
    objectBindings.put("botTank", Tank.class);
    objectBindings.put("land", Land.class);
    objectBindings.put("bullet", Bullet.class);
    objectBindings.put("building", Decor.class);
    objectBindings.put("aidkit", AidKit.class);

    subprogramBindings = new HashMap<>();
    subprogramBindings.put("bot", BotSubprogram.class);

    maps = loadMapNames();
  }

  public MapDescription getDescription() { return description; }
  public Map getMap() { return map; }

  public MapDescription load(String name)
  {
    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    IWorld world = GameContext.content.getWorld();
    MapDescription description = Serializer.Deserialize(String.format("Maps/%s.map", name));

    for (MapDescription.MapObject obj : description.objects)
    {
      EngineObject object = createObject(obj.name);
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

    this.description = description;
    this.map = new Map(description.size);
    return description;
  }

  public List<String> getMaps() { return maps; }

  private EngineObject createObject(String name)
  {
    try
    {
      Class<?> objectClass = objectBindings.get(name);
      Constructor<?> constructor = objectClass.getConstructor(String.class);
      if (constructor != null)
        return (EngineObject) constructor.newInstance(name);

      throw new IllegalStateException("Object not contains constructor with string parameter");
    }
    catch (Exception e)
    {
      throw new RuntimeException(String.format("Can't create object with %s name", name), e);
    }
  }

  private ISubprogram createSubprogram(String name, EngineObject bindObject)
  {
    try
    {
      Class<?> subprogramClass = subprogramBindings.get(name);
      Constructor<?> gameObjectCtor = subprogramClass.getConstructor(GameObject.class);
      if (gameObjectCtor != null)
        return (ISubprogram) gameObjectCtor.newInstance((GameObject) bindObject);

      Constructor<?> engineObjectCtor = subprogramClass.getConstructor(EngineObject.class);
      if (engineObjectCtor != null)
        return (ISubprogram) engineObjectCtor.newInstance(bindObject);

      Constructor<?> emptyCtor = subprogramClass.getConstructor();
      if (emptyCtor != null)
        return (ISubprogram) emptyCtor.newInstance();

      throw new IllegalArgumentException("Object not contains right constructor with one of parameter: GameObject, EngineObject. Or it may be empty.");
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
