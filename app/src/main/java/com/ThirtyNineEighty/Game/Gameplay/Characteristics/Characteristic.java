package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

public class Characteristic
{
  private double health;

  public Characteristic(Characteristic other)
  {
    health = other.getHealth();
  }

  public double getHealth() { return health; }
  public void setHealth(double value) { health = value; }
}
