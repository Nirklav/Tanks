package com.ThirtyNineEighty.Game.Map;

import android.content.res.AssetManager;

import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.List;

public final class MapManager
{
  private ArrayList<String> maps;

  private MapFactory factory;
  private Map map;

  public void initialize()
  {
    factory = new MapFactory();
    maps = loadMapNames();
  }

  public Map getMap() { return map; }

  public Tank load(GameStartArgs args)
  {
    String name = args.getMapName();
    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    IWorld world = GameContext.content.getWorld();
    MapDescription description = Serializer.Deserialize(String.format("Maps/%s.map", name));
    map = new Map(description.size);

    // Create objects
    for (MapDescription.Object obj : description.objects)
    {
      EngineObject object = factory.createObject(obj.name);
      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      // Create object subprograms
      createSubprograms(object, obj.subprograms, object);

      world.add(object);
    }

    // Create map subprograms
    createSubprograms(world, description.subprograms, null);

    // Create player
    Tank player = new Tank(args.getTankName());
    player.setPosition(description.player.getPosition());
    player.setAngles(description.player.getAngles());
    world.add(player);

    return player;
  }

  private void createSubprograms(IBindable bindable, String[] subprograms, Object parameter)
  {
    if (subprograms == null)
      return;

    for (String subprogramName : subprograms)
      bindable.bindProgram(factory.createSubprogram(subprogramName, parameter));
  }

  public List<String> getMaps() { return maps; }

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
