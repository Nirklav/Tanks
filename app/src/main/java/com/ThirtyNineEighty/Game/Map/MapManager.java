package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Data.Entities.*;
import com.ThirtyNineEighty.Game.Map.Descriptions.*;
import com.ThirtyNineEighty.Game.Map.Factory.*;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Worlds.*;
import com.ThirtyNineEighty.Common.Serializer;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.*;

import java.util.ArrayList;

public final class MapManager
{
  private static final String SavedGame = "savedGame";

  private MapFactory factory;
  private Map map;

  public void initialize()
  {
    factory = new MapFactory();
  }

  public Map getMap() { return map; }

  public GameWorld create(GameStartArgs args)
  {
    ArrayList<String> maps = GameContext.resources.getContent(new FileContentSource("maps"));
    String name = args.getMapName();

    if (!maps.contains(name))
      throw new IllegalArgumentException("name");

    MapDescription description = Serializer.Deserialize(String.format("Maps/%s.map", name));
    map = new Map(name, description);

    // Create player
    Tank player = new Tank(args.getTankName(), args.getProperties());
    player.setPosition(description.player.getPosition());
    player.setAngles(description.player.getAngles());

    GameWorld world = new GameWorld(player);

    // Create objects
    for (MapObject obj : description.objects)
    {
      WorldObject object = factory.createObject(obj.type, obj.type, obj.properties);

      object.setPosition(obj.getPosition());
      object.setAngles(obj.getAngles());

      // Create object subprograms
      createSubprograms(object, obj.subprograms, object);

      world.add(object);
    }

    // Create map subprograms
    createSubprograms(world, description.subprograms, null);

    return world;
  }

  private void createSubprograms(IBindable bindable, String[] subprograms, Object parameter)
  {
    if (subprograms == null)
      return;

    for (String subprogramName : subprograms)
      bindable.bind(factory.createSubprogram(subprogramName, subprogramName, parameter));
  }

  public void save()
  {
    IWorld world = GameContext.content.getWorld();
    if (world == null)
      return;

    if (world instanceof GameWorld)
    {
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
  }

  public GameWorld load()
  {
    SavedGameEntity game = GameContext.gameProgress.getSavedGame(SavedGame);
    if (game == null)
      return null;

    MapDescription description = Serializer.Deserialize(String.format("Maps/%s.map", game.mapName));
    map = new Map(game.mapName, description);

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
