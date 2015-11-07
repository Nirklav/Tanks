package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;

public class Decor
  extends WorldObject<Description, Properties>
{
  private static final long serialVersionUID = 1L;

  public Decor(String type)
  {
    super(type, new Properties());
  }
}
