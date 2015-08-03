package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Menu.MainMenu;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;

public class KillMarkedSubprogram
  extends Subprogram
{
  private ArrayList<GameObject> marked;

  @Override
  protected void onUpdate()
  {
    Map map = GameContext.mapManager.getMap();
    IWorld world = GameContext.content.getWorld();
    GameObject player = (GameObject) world.getPlayer();

    if (map.getState() != Map.StateInProgress)
    {
      unbind();
      GameContext.content.setWorld(null);
      GameContext.content.setMenu(new MainMenu());
      return;
    }

    // Check lose
    if (player.getHealth() <= 0)
    {
      map.setState(Map.StateLose);
      delay(5000);
      return;
    }

    // Check win
    if (marked == null)
    {
      marked = new ArrayList<>();
      ArrayList<EngineObject> worldObjects = new ArrayList<>();
      world.fillObjects(worldObjects);

      for (int i = worldObjects.size() - 1; i >= 0; i--)
      {
        EngineObject object = worldObjects.get(i);
        if (object instanceof GameObject)
        {
          GameObject gameObject = (GameObject) object;
          GameProperties properties = gameObject.getProperties();
          if (properties.needKill())
            marked.add(gameObject);
        }
      }
    }

    for (int i = marked.size() - 1; i >= 0; i--)
    {
      GameObject current = marked.get(i);
      if (!current.isInitialized())
        marked.remove(i);
    }

    if (marked.size() == 0)
    {
      map.setState(Map.StateWin);
      delay(5000);
    }
  }
}
