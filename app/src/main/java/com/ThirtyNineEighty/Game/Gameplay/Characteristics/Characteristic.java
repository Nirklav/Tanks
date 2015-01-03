package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

public class Characteristic
{
  public final String visualModelName;
  public final String phModelName;
  public final String textureName;

  private float health;
  private float speed; // m / s

  public Characteristic(String visualModelName, String phModelName, String textureName)
  {
    this.visualModelName = visualModelName;
    this.phModelName = phModelName;
    this.textureName = textureName;
  }

  public Characteristic(Characteristic other)
  {
    visualModelName = other.visualModelName;
    phModelName = other.phModelName;
    textureName = other.textureName;

    health = other.getHealth();
    speed = other.getSpeed();
  }

  public float getHealth() { return health; }
  public void setHealth(float value) { health = value; }

  public void subtractHealth(float value) { health -= value; }
  public void addHealth(float value) { health += value; }

  public float getSpeed() { return speed; }
  public void setSpeed(float value) { speed = value; }
}
