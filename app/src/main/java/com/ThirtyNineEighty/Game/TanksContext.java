package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Game.ContentState.ContentStateManager;
import com.ThirtyNineEighty.Game.Data.TanksDataManager;
import com.ThirtyNineEighty.Game.Resources.TanksResources;

public class TanksContext
  extends GameContext
{
  public static TanksDataManager data;
  public static TanksResources resources;
  public static ContentStateManager contentState;
}
