package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.LoadingMenu;
import com.ThirtyNineEighty.Game.TanksContext;

public class MainLoadingState
  implements IContentState
{
  private IWorld saved;

  public MainLoadingState(IWorld saved)
  {
    this.saved = saved;
  }

  @Override
  public ContentStateType getType()
  {
    return ContentStateType.MAIN_LOADING;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev != null)
      throw new IllegalArgumentException("Wrong prev state");

    TanksContext.content.setMenu(new LoadingMenu());
    TanksContext.content.setWorld(saved);
    saved.disable();
  }

  @Override
  public IContentState buildNext()
  {
    return new MainState(false);
  }
}
