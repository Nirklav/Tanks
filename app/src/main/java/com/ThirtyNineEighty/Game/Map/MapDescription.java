package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Helpers.Vector3;

import java.io.Serializable;

@SuppressWarnings("UnusedDeclaration") // fields set by serialization
public class MapDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static class Object
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public String name;
    public String[] subprograms;
    public boolean needKill;

    private float xPos;
    private float yPos;
    private float zPos;

    private float xAng;
    private float yAng;
    private float zAng;

    public Vector3 getPosition() { return new Vector3(xPos, yPos, zPos); }
    public Vector3 getAngles() { return new Vector3(xAng, yAng, zAng); }
  }

  public static class Player
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    private float xPos;
    private float yPos;
    private float zPos;

    private float xAng;
    private float yAng;
    private float zAng;

    public Vector3 getPosition() { return new Vector3(xPos, yPos, zPos); }
    public Vector3 getAngles() { return new Vector3(xAng, yAng, zAng); }
  }

  public float size;
  public Object[] objects;
  public Player player;
  public String[] subprograms;

  public String[] openingMaps;
  public String[] openingTanks;
  public String[] openingUpgrades;
}
