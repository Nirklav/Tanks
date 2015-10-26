package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Properties.Properties;

public class Land
  extends WorldObject
{
  public static final float size = 50.0f;

  public Land()
  {
    super(null, "land", new Properties());
  }

  public Land(ObjectState state)
  {
    super(state);
  }
}
