package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.MainMenu;
import com.ThirtyNineEighty.Game.TanksContext;

public class MainState
  implements IContentState
{
  private boolean clearWorld;

  public MainState(boolean clearWorld)
  {
    this.clearWorld = clearWorld;
  }

  @Override
  public ContentStateType getType()
  {
    return ContentStateType.MAIN;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
    {
      TanksContext.content.setMenu(new MainMenu());
      return;
    }

    switch (prev.getType())
    {
    case MAIN_LOADING:
      TanksContext.content.setMenu(new MainMenu());
      return;

    case MAP_SELECT:
    case TANK_SELECT:
    case GAME:
    case EDITOR:
      TanksContext.content.setMenu(new MainMenu());
      if (clearWorld)
        TanksContext.content.setWorld(null);
      else
      {
        IWorld world = TanksContext.content.getWorld();
        if (world != null)
          world.disable();
      }
      return;
    }
    throw new IllegalArgumentException("Wrong prev state");
  }

  @Override
  public IContentState buildNext()
  {
    return null;
  }
}
