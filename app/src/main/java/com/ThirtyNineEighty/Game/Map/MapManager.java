package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Map.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Subprograms.RechargeSubprogram;
import com.ThirtyNineEighty.Game.Worlds.*;
import com.ThirtyNineEighty.Common.Serializer;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.Resources.Sources.FileMapDescriptionSource;
import com.ThirtyNineEighty.System.*;

import java.util.ArrayList;

public final class MapManager
{
  private Map map;

  public Map getMap() { return map; }

  public GameWorld create(GameStartArgs args)
  {
    ArrayList<String> maps = GameContext.resources.getContent(new FileContentSource("maps"));
    String name = args.getMapName();

    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    MapDescription mapDesc = GameContext.resources.getMap(new FileMapDescriptionSource(name));
    map = new Map(name, mapDesc);

    // Create player
    Tank player = new Tank(args.getTankName(), args.getProperties());
    player.setPosition(mapDesc.player.getPosition());
    player.setAngles(mapDesc.player.getAngles());
    player.bind(new RechargeSubprogram(player));

    // Create world
    GameWorld world = new GameWorld(player);

    // Create objects
    for (MapObject mapObj : mapDesc.objects)
      world.add(create(mapObj));

    // Create map subprograms
    for (String subprogramName : mapDesc.subprograms)
      world.bind(GameContext.factory.createSubprogram(subprogramName, subprogramName));

    return world;
  }

  private WorldObject<?, ?> create(MapObject mapObj)
  {
    Description description = GameContext.resources.getDescription(new FileDescriptionSource(mapObj.description));
    WorldObject<?, ?> object = GameContext.factory.createObject(description.getObjectType(), mapObj.description, mapObj.properties);

    object.setPosition(mapObj.getPosition());
    object.setAngles(mapObj.getAngles());

    for (String subprogramName : mapObj.subprograms)
      object.bind(GameContext.factory.createSubprogram(subprogramName, subprogramName, object));

    return object;
  }

  public void load(String mapName)
  {
    MapDescription mapDesc = Serializer.Deserialize(String.format("Maps/%s.map", mapName));
    map = new Map(mapName, mapDesc);
  }

  public void reset()
  {
    map = null;
  }
}
