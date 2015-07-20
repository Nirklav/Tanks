package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapObject;
import com.ThirtyNineEighty.Game.Map.Factory.MapFactory;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
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
    for (MapObject obj : description.objects)
    {
      EngineObject object = factory.createObject(obj.type, obj.properties);

      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      // Create object subprograms
      createSubprograms(object, obj.subprograms, object);

      world.add(object);
    }

    // Create map subprograms
    createSubprograms(world, description.subprograms, null);

    // Create player
    GameProperties properties = new GameProperties(args.getBulletName(), null);
    Tank player = new Tank(args.getTankName(), properties);
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
