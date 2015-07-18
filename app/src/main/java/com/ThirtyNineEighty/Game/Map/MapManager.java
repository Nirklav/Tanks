package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public final class MapManager
{
  private MapFactory factory;
  private Map map;

  public void initialize()
  {
    factory = new MapFactory();
  }

  public Map getMap() { return map; }

  public Tank load(GameStartArgs args)
  {
    ArrayList<String> maps = GameContext.resources.getContent(new FileContentSource("maps"));
    String name = args.getMapName();

    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    IWorld world = GameContext.content.getWorld();
    MapDescription description = Serializer.Deserialize(String.format("Maps/%s.map", name));
    map = new Map(description);

    // Create objects
    for (MapDescription.Object obj : description.objects)
    {
      EngineObject object = factory.createObject(obj.type, obj.bulletType);

      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());
      object.properties.needKill = obj.needKill;

      // Create object subprograms
      createSubprograms(object, obj.subprograms, object);

      world.add(object);
    }

    // Create map subprograms
    createSubprograms(world, description.subprograms, null);

    // Create player
    Tank player = new Tank(args.getTankName(), args.getBulletName());
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
}
