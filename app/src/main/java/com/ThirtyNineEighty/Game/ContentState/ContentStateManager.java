package com.ThirtyNineEighty.Game.ContentState;

import com.ThirtyNineEighty.Game.ContentState.States.IContentState;

public class ContentStateManager
{
  private IContentState current;

  public void set(IContentState state)
  {
    if (state == null)
      throw new IllegalArgumentException("state == null");

    IContentState prev = current;
    current = state;

    try
    {
      current.apply(prev);
    }
    catch (Exception e)
    {
      String msg = String.format("Error on apply. Prev state: %s. New state: %s", prev.getType(), current.getType());
      throw new RuntimeException(msg, e);
    }
  }

  public void next()
  {
    if (current == null)
      throw new IllegalStateException("current state not set");

    IContentState next = current.buildNext();
    if (next == null)
      throw new IllegalStateException("for current state following not implemented");

    set(next);
  }
}
