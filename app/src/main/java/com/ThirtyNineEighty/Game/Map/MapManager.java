package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Data.Entities.*;
import com.ThirtyNineEighty.Game.Map.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Worlds.*;
import com.ThirtyNineEighty.Common.Serializer;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.*;

import java.util.ArrayList;

public final class MapManager
{
  private static final String SavedGame = "savedGame";

  private Map map;

  public Map getMap() { return map; }

  public GameWorld create(GameStartArgs args)
  {
    ArrayList<String> maps = GameContext.resources.getContent(new FileContentSource("maps"));
    String name = args.getMapName();

    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    MapDescription mapDesc = Serializer.Deserialize(String.format("Maps/%s.map", name));
    map = new Map(name, mapDesc);

    // Create player
    Tank player = new Tank("Player", args.getTankName(), args.getProperties());
    player.setPosition(mapDesc.player.getPosition());
    player.setAngles(mapDesc.player.getAngles());

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

  private WorldObject create(MapObject mapObj)
  {
    WorldObject object = GameContext.factory.createObject(mapObj.type, mapObj.type, mapObj.properties);

    object.setPosition(mapObj.getPosition());
    object.setAngles(mapObj.getAngles());

    for (String subprogramName : mapObj.subprograms)
      object.bind(GameContext.factory.createSubprogram(subprogramName, subprogramName, object));

    return object;
  }

  public void save()
  {
    IWorld world = GameContext.content.getWorld();
    if (world == null || !(world instanceof GameWorld))
    {
      GameContext.gameProgress.deleteGame(SavedGame);
      return;
    }

    WorldObject player = world.getPlayer();
    SavedGameEntity game = new SavedGameEntity();

    game.mapName = map.name;
    game.player = new SavedObject(player);
    game.objects = new ArrayList<>();
    game.worldSubprograms = new ArrayList<>();

    for (ISubprogram subprogram : world.getSubprograms())
      game.worldSubprograms.add(new SavedSubprogram(subprogram));

    ArrayList<WorldObject> worldObjects = new ArrayList<>();
    world.getObjects(worldObjects);

    for (WorldObject object : worldObjects)
    {
      String playerName = player.getName();
      String objectName = object.getName();

      // Skip player, we already serialized it
      if (playerName.equals(objectName))
        continue;

      game.objects.add(new SavedObject(object));
    }

    GameContext.gameProgress.saveGame(SavedGame, game);
  }

  public GameWorld load()
  {
    SavedGameEntity game = GameContext.gameProgress.getSavedGame(SavedGame);
    if (game == null)
      return null;

    MapDescription mapDesc = Serializer.Deserialize(String.format("Maps/%s.map", game.mapName));
    map = new Map(game.mapName, mapDesc);

    try
    {
      Tank player = (Tank) game.player.load();
      GameWorld world = new GameWorld(player);

      for (SavedObject saved : game.objects)
        world.add(saved.load());

      for (SavedSubprogram saved : game.worldSubprograms)
        world.bind(saved.load(null));

      return world;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
