package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;

public class KillMarkedSubprogram
  extends Subprogram
{
  private ArrayList<EngineObject> marked;

  @Override
  protected void onUpdate()
  {
    Map map = GameContext.mapManager.getMap();
    IWorld world = GameContext.content.getWorld();
    GameObject player = (GameObject) world.getPlayer();
    Characteristic playerCharacteristic = player.getCharacteristic();

    // Check lose
    if (playerCharacteristic.getHealth() <= 0)
    {
      map.setState(Map.StateLose);
      unbind();
      return;
    }

    // Check win
    if (marked == null)
    {
      world.fillObjects(marked = new ArrayList<>());

      for (int i = marked.size() - 1; i >= 0; i--)
      {
        EngineObject current = marked.get(i);
        if (!current.properties.needKill)
          marked.remove(i);
      }
    }

    for (int i = marked.size() - 1; i >= 0; i--)
    {
      EngineObject current = marked.get(i);
      if (!current.isInitialized())
        marked.remove(i);
    }

    if (marked.size() == 0)
    {
      map.setState(Map.StateWin);
      unbind();
    }
  }
}
