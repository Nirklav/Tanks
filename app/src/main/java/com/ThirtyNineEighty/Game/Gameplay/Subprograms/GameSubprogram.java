package com.ThirtyNineEighty.Game.Gameplay.Subprograms;

import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

public abstract class GameSubprogram extends Subprogram
{
  protected GameWorld world;

  protected GameSubprogram()
  {
    this.world = (GameWorld)GameContext.getContent().getWorld();
  }
}
