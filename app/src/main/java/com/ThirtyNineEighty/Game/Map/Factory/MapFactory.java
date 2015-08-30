package com.ThirtyNineEighty.Game.Map.Factory;

import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Subprograms.*;
import com.ThirtyNineEighty.System.ISubprogram;

import java.util.HashMap;

public class MapFactory
{
  private HashMap<String, Creator<? extends WorldObject>> objectCreators;
  private HashMap<String, Creator<? extends ISubprogram>> subprogramsCreators;

  public MapFactory()
  {
    objectCreators = new HashMap<>();
    subprogramsCreators = new HashMap<>();

    addObject("tank", Tank.class);
    addObject("botTank", Tank.class);
    addObject("land", Land.class);
    addObject("bullet", Bullet.class);
    addObject("building", Decor.class);
    addObject("aidKit", AidKit.class);

    addSubprogram("bot", BotSubprogram.class);
    addSubprogram("move", MoveSubprogram.class);
    addSubprogram("killMarkedCompletion", KillMarkedSubprogram.class);
    addSubprogram("rechargeSubprogram", RechargeSubprogram.class);
  }

  public WorldObject createObject(String type, Object... params)
  {
    Creator<? extends WorldObject> creator = objectCreators.get(type);
    return creator.create(params);
  }

  public ISubprogram createSubprogram(String type, Object... params)
  {
    Creator<? extends ISubprogram> creator = subprogramsCreators.get(type);
    return creator.create(params);
  }

  private void addObject(String name, Class<? extends WorldObject> type)
  {
    objectCreators.put(name, new Creator<>(type));
  }

  private void addSubprogram(String name, Class<? extends ISubprogram> type)
  {
    subprogramsCreators.put(name, new Creator<>(type));
  }
}
