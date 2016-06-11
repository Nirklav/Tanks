package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Game.TanksContext;

public class GameState
  implements IContentState
{
  private final IWorld world;

  public GameState()
  {
    this(null);
  }

  public GameState(IWorld world)
  {
    this.world = world;
  }

  @Override
  public ContentStateType getType()
  {
    return ContentStateType.GAME;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case MAIN:
      TanksContext.content.setMenu(new GameMenu());
      world.enable();
      return;
    case GAME_LOADING:
      TanksContext.content.setMenu(new GameMenu());
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
