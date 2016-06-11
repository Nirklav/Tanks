package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.LoadingMenu;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;

public class GameLoadingState
  implements IContentState
{
  private final GameStartArgs args;

  public GameLoadingState(GameStartArgs args)
  {
    this.args = args;
  }

  @Override
  public ContentStateType getType()
  {
    return ContentStateType.GAME_LOADING;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case TANK_SELECT:
      TanksContext.content.setMenu(new LoadingMenu());
      TanksContext.content.setWorld(new GameWorld(args));
      return;
    }

    throw new IllegalArgumentException("Wrong prev state");
  }

  @Override
  public IContentState buildNext()
  {
    return new GameState();
  }
}
