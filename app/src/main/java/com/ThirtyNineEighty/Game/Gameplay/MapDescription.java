package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Helpers.Vector3;

import java.io.Serializable;

@SuppressWarnings("UnusedDeclaration") // fields set by serialization
public class MapDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static class MapObject
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public String name;
    public String[] subprograms;

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
  public MapObject[] objects;
  public Player player;
}
