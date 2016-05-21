package com.ThirtyNineEighty.Base.Objects.Properties;

import java.io.Serializable;

public class Properties
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean staticObject;
  private boolean ignoreOnMap;

  public boolean isStaticObject()
  {
    return staticObject;
  }

  public boolean isIgnoreOnMap()
  {
    return ignoreOnMap;
  }

  public void setStaticObject(boolean value)
  {
    staticObject = value;
  }

  public void setIgnoreOnMap(boolean value)
  {
    ignoreOnMap = value;
  }
}
