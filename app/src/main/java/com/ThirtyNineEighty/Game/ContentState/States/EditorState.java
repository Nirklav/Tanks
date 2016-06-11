package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.EditorMenu;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.EditorWorld;

public class EditorState
  implements IContentState
{
  @Override
  public ContentStateType getType()
  {
    return ContentStateType.EDITOR;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case MAIN:
      TanksContext.content.setMenu(new EditorMenu());
      TanksContext.content.setWorld(new EditorWorld());
      return;
    case EDITOR_IMPORT:
      TanksContext.content.setMenu(new EditorMenu());
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
