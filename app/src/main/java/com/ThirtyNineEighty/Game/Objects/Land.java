package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;

public class Land
  extends WorldObject<Description, Properties>
{
  private static final long serialVersionUID = 1L;

  public static final String sand = "sandLand";
  public static final float size = 50.0f;

  public Land(String descriptionName)
  {
    super(descriptionName, new Properties());
  }
}
