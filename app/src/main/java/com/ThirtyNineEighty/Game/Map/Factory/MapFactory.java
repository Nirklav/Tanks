package com.ThirtyNineEighty.Game.Map.Factory;

import com.ThirtyNineEighty.Game.Objects.AidKit;
import com.ThirtyNineEighty.Game.Objects.Bullet;
import com.ThirtyNineEighty.Game.Objects.Decor;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Subprograms.BotSubprogram;
import com.ThirtyNineEighty.Game.Subprograms.KillMarkedSubprogram;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.System.ISubprogram;

import java.util.HashMap;

public class MapFactory
{
  private HashMap<String, Creator<?>> objectCreators;
  private HashMap<String, Creator<?>> subprogramBindings;

  public MapFactory()
  {
    objectCreators = new HashMap<>();
    objectCreators.put("tank", new Creator<>(Tank.class, String.class, GameProperties.class));
    objectCreators.put("botTank", new Creator<>(Tank.class, String.class, GameProperties.class));
    objectCreators.put("land", new Creator<>(Land.class));
    objectCreators.put("bullet", new Creator<>(Bullet.class, String.class));
    objectCreators.put("building", new Creator<>(Decor.class, String.class));
    objectCreators.put("aidKit", new Creator<>(AidKit.class, String.class));

    subprogramBindings = new HashMap<>();
    subprogramBindings.put("bot", new Creator<>(BotSubprogram.class, GameObject.class));
    subprogramBindings.put("move", new Creator<>(MoveSubprogram.class, GameObject.class));
    subprogramBindings.put("killMarkedCompletion", new Creator<>(KillMarkedSubprogram.class));
  }

  public WorldObject createObject(String type, Properties properties)
  {
    Creator<?> creator = objectCreators.get(type);
    return (WorldObject)creator.create(type, properties);
  }

  public ISubprogram createSubprogram(String type, Object parameter)
  {
    Creator<?> creator = subprogramBindings.get(type);
    return (ISubprogram)creator.create(type, parameter);
  }
}
