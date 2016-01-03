package com.ThirtyNineEighty.Game.Map.Descriptions;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;

public class MapObject
  extends MapState
{
  private static final long serialVersionUID = 1L;

  public String description;
  public String[] subprograms;
  public Properties properties;

  public MapObject(Vector3 position, Vector3 angles)
  {
    super(position, angles);
  }
}
