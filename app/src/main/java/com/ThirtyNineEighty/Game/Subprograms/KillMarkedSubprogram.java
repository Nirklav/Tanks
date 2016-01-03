package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;

import java.util.ArrayList;

public class KillMarkedSubprogram
  extends WinConditionSubprogram
{
  private static final long serialVersionUID = 1L;

  private ArrayList<GameObject<?, ?>> marked;

  public KillMarkedSubprogram(GameWorld world)
  {
    super(world);
  }

  @Override
  protected void checkCondition()
  {
    GameWorld world = (GameWorld) TanksContext.content.getWorld();
    GameObject<?, ?> player = (GameObject<?, ?>) world.getPlayer();
    
    // Check lose
    if (player.getHealth() <= 0)
    {
      world.setState(this, GameWorld.State.failed);
      unbind();
      return;
    }

    // Check win
    if (marked == null)
    {
      marked = new ArrayList<>();
      ArrayList<WorldObject<?, ?>> worldObjects = new ArrayList<>();
      world.getObjects(worldObjects);

      for (int i = worldObjects.size() - 1; i >= 0; i--)
      {
        WorldObject<?, ?> object = worldObjects.get(i);
        if (object instanceof GameObject)
        {
          GameObject<?, ?> gameObject = (GameObject<?, ?>) object;
          GameProperties properties = gameObject.getProperties();
          if (properties.needKill())
            marked.add(gameObject);
        }
      }
    }

    for (int i = marked.size() - 1; i >= 0; i--)
    {
      GameObject<?, ?> current = marked.get(i);
      if (current.getHealth() <= 0 || !current.isInitialized())
        marked.remove(i);
    }

    if (marked.size() == 0)
    {
      world.setState(this, GameWorld.State.completed);
      unbind();
    }
  }
}
