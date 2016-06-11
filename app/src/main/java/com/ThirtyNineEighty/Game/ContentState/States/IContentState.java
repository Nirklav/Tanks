package com.ThirtyNineEighty.Game.ContentState.States;

import com.ThirtyNineEighty.Game.ContentState.ContentStateType;

public interface IContentState
{
  ContentStateType getType();
  IContentState buildNext();
  void apply(IContentState prev);
}
