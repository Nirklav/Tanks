package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;

public class Land
  extends WorldObject<Description, Properties>
{
  private static final long serialVersionUID = 1L;

  public static final float size = 50.0f;

  public Land()
  {
    super("land", new Properties());
  }
}
