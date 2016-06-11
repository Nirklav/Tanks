package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.TankSelectMenu;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;

public class TankSelectState
  implements IContentState
{
  private final GameStartArgs args;

  public TankSelectState(GameStartArgs args)
  {
    if (args == null)
      throw new IllegalArgumentException("args is null");

    this.args = args;
  }

  @Override
  public ContentStateType getType()
  {
    return ContentStateType.TANK_SELECT;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case MAP_SELECT:
      TanksContext.content.setMenu(new TankSelectMenu(args));
      TanksContext.content.setWorld(new TankSelectWorld(args));
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
