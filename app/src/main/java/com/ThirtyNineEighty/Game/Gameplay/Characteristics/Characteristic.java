package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

import com.ThirtyNineEighty.Game.EngineObjectDescription;

public class Characteristic
{
  public final EngineObjectDescription initializer;

  private float health;
  private float speed; // m/s
  private float damage;
  private float rotationSpeed;

  public Characteristic(EngineObjectDescription init)
  {
    initializer = init;
  }

  public Characteristic(Characteristic other)
  {
    initializer = other.initializer;

    health = other.getHealth();
    speed = other.getSpeed();
    damage = other.getDamage();
    rotationSpeed = other.getRotationSpeed();
  }

  public float getHealth() { return health; }
  public void setHealth(float value) { health = value; }

  public void subtractHealth(float value) { health -= value; }
  public void addHealth(float value) { health += value; }

  public float getSpeed() { return speed; }
  public void setSpeed(float value) { speed = value; }

  public float getDamage() { return damage; }
  public void setDamage(float value) { damage = value; }

  public float getRotationSpeed() { return rotationSpeed; }
  public void setRotationSpeed(float value) { rotationSpeed = value; }
}
