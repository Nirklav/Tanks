package com.ThirtyNineEighty.Base.Objects.Properties;

import java.io.Serializable;

public class Properties
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean staticObject;

  public boolean isStaticObject()
  {
    return staticObject;
  }

  public void setStaticObject(boolean value)
  {
    staticObject = value;
  }
}
