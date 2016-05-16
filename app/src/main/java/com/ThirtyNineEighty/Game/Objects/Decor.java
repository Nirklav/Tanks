package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;

public class Decor
  extends WorldObject<Description, Properties>
{
  private static final long serialVersionUID = 1L;

  public Decor(String descriptionName)
  {
    super(descriptionName, new Properties());
  }

  public Decor(String descriptionName, Properties properties)
  {
    super(descriptionName, properties);
  }
}
