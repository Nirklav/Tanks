package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Base.Subprograms.ISubprogram;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Base.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Base.Worlds.BaseWorld;
import com.ThirtyNineEighty.Base.Renderable.Common.*;
import com.ThirtyNineEighty.Game.ContentState.States.MainState;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapObject;
import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Resources.Sources.FileMapDescriptionSource;
import com.ThirtyNineEighty.Game.Subprograms.*;
import com.ThirtyNineEighty.Game.TanksContext;

import java.util.HashMap;

public class GameWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  private static final float lightHeight = 100;
  private static final float lightX = 50;
  private static final float lightY = 50;

  public final static int inProgress = 0;
  public final static int win = 1;
  public final static int lose = 2;

  private final HashMap<Long, State> states = new HashMap<>();

  private int lastState;
  private boolean lastStateChanged;

  private String mapName;
  private transient Map map;

  public GameWorld(GameStartArgs args)
  {
    mapName = args.getMapName();

    ContentNames maps = TanksContext.resources.getContent(new FileContentSource(FileContentSource.maps));
    if (!maps.names.contains(mapName))
      throw new IllegalArgumentException("name");

    MapDescription description = TanksContext.resources.getMap(new FileMapDescriptionSource(mapName));

    // Create player
    final Tank player = new Tank(args.getTankName(), args.getProperties());
    this.player = player;

    player.setPosition(description.player.getPosition());
    player.setAngles(description.player.getAngles());
    player.bind(new RechargeSubprogram(player));
    add(player);

    // Create objects
    for (MapObject mapObj : description.objects)
      add(create(mapObj));

    // Create map subprograms
    for (String subprogramName : description.subprograms)
      bind(TanksContext.factory.createSubprogram(subprogramName, this));

    // Bind camera and light
    bind(new Camera(new Camera.Setter()
    {
      @Override
      public void set(Camera.Data camera)
      {
        camera.target.setFrom(player.getPosition());
        camera.eye.setFrom(player.getPosition());

        camera.eye.addToY(14);
        camera.eye.addToZ(35);
      }
    }));

    bind(new Light(new Light.Setter()
    {
      @Override
      public void set(Light.Data light)
      {
        light.Position.setFrom(lightX, lightY, lightHeight);
      }
    }));

    // Bind gameWorld update
    bind(new Subprogram()
    {
      private static final long serialVersionUID = 1L;
      private boolean clearWorld;

      @Override
      protected void onUpdate()
      {
        if (clearWorld)
        {
          TanksContext.contentState.set(new MainState(true));
          unbind();
          return;
        }

        int state = getState();
        if (state != inProgress)
        {
          if (map.description.openingMaps != null)
            for (String name : map.description.openingMaps)
              TanksContext.data.openMap(name);

          if (map.description.openingTanks != null)
            for (String name : map.description.openingTanks)
              TanksContext.data.openTank(name);

          if (map.description.openingUpgrades != null)
            for (String name : map.description.openingUpgrades)
              TanksContext.data.openUpgrade(name);

          clearWorld = true;
          delay(5000);
        }
      }
    });

    TanksContext.resources.release(description);
    TanksContext.resources.release(maps);
  }

  private WorldObject<?, ?> create(MapObject mapObj)
  {
    WorldObject<?, ?> object = TanksContext.factory.createObject(mapObj.description, mapObj.properties);

    object.setPosition(mapObj.getPosition());
    object.setAngles(mapObj.getAngles());

    for (String subprogramName : mapObj.subprograms)
    {
      ISubprogram subprogram = TanksContext.factory.createSubprogram(subprogramName, object);
      object.bind(subprogram);
    }

    return object;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    // Create map
    map = new Map(mapName);

    // Loading is over, set next state
    TanksContext.contentState.next();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    // Release map
    map.release();
  }

  @Override
  public IMap getMap()
  {
    return map;
  }

  @Override
  public boolean needSave()
  {
    return true;
  }

  public void setState(WinConditionSubprogram subprogram, State state)
  {
    lastStateChanged = true;

    Long id = subprogram.getId();
    states.put(id, state);
  }

  public int getState()
  {
    if (!lastStateChanged)
      return lastState;

    if (states.size() == 0)
      return inProgress;

    int result = win;

    for (State state : states.values())
    {
      if (state == State.failed)
      {
        result = lose;
        break;
      }

      if (state == State.inProgress)
      {
        result = inProgress;
        break;
      }
    }

    lastState = result;
    return result;
  }

  public enum State
  {
    inProgress,
    completed,
    failed
  }
}
