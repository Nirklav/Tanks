package com.ThirtyNineEighty.Game.Map.Descriptions;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;

import java.io.Serializable;

public class MapState
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private float xPos;
  private float yPos;
  private float zPos;

  private float xAng;
  private float yAng;
  private float zAng;

  public MapState(Vector3 position, Vector3 angles)
  {
    xPos = position.getX();
    yPos = position.getY();
    zPos = position.getZ();

    xAng = angles.getX();
    yAng = angles.getY();
    zAng = angles.getZ();
  }

  public Vector3 getPosition() { return new Vector3(xPos, yPos, zPos); }
  public Vector3 getAngles() { return new Vector3(xAng, yAng, zAng); }
}
