package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.MapSelectMenu;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;

public class MapSelectState
  implements IContentState
{
  @Override
  public ContentStateType getType()
  {
    return ContentStateType.MAP_SELECT;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case MAIN:
      TanksContext.content.setMenu(new MapSelectMenu(new GameStartArgs()));
      TanksContext.content.setWorld(null);
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
