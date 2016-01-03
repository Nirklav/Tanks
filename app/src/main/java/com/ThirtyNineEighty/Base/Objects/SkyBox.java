package com.ThirtyNineEighty.Base.Objects;

import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Objects.Properties.SkyBoxProperties;

public class SkyBox
  extends WorldObject<Description, SkyBoxProperties>
{
  public static final String desert = "desert";

  public SkyBox(String descriptionName, SkyBoxProperties properties)
  {
    super(descriptionName, properties);
  }
}
