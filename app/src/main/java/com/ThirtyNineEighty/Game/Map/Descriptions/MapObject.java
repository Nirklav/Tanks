package com.ThirtyNineEighty.Game.Map.Descriptions;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;

public class MapObject
  extends MapState
{
  private static final long serialVersionUID = 1L;

  public String type;
  public String[] subprograms;
  public Properties properties;

  public MapObject(Vector3 position, Vector3 angles)
  {
    super(position, angles);
  }
}
