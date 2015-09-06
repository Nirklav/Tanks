package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.System.State;

public class Decor
  extends WorldObject
{
  public Decor(State state)
  {
    super(state);
  }

  public Decor(String type)
  {
    super(null, type, new Properties());
  }
}
