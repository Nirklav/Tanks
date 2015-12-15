package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Properties.SkyBoxProperties;

public class SkyBox
  extends WorldObject<Description, SkyBoxProperties>
{
  public static final String desert = "desert";

  public SkyBox(String descriptionName, SkyBoxProperties properties)
  {
    super(descriptionName, properties);
  }
}
