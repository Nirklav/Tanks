package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;
import com.ThirtyNineEighty.Game.Menu.EditorImportMenu;
import com.ThirtyNineEighty.Game.TanksContext;

public class EditorImportState
  implements IContentState
{
  @Override
  public ContentStateType getType()
  {
    return ContentStateType.EDITOR_IMPORT;
  }

  @Override
  public void apply(IContentState prev)
  {
    if (prev == null)
      throw new IllegalArgumentException("Wrong prev state");

    switch (prev.getType())
    {
    case EDITOR:
      TanksContext.content.setMenu(new EditorImportMenu());
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
