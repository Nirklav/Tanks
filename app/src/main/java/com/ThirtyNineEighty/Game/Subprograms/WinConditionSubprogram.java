package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;

public abstract class WinConditionSubprogram
  extends Subprogram
{
  protected GameWorld world;

  public WinConditionSubprogram(GameWorld world)
  {
    this.world = world;
  }

  @Override
  protected final void onUpdate()
  {
    if (world.getState() != GameWorld.inProgress)
    {
      unbind();
      return;
    }

    checkCondition();
  }

  @Override
  public void initialize()
  {
    super.initialize();

    world.setState(this, GameWorld.State.inProgress);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();
  }

  protected abstract void checkCondition();
}
