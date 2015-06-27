package com.ThirtyNineEighty.Game.Characteristics;

import com.ThirtyNineEighty.Game.Objects.EngineObjectDescription;

import java.io.Serializable;

public class Characteristic
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static final float maxRechargeLevel = 100;

  public final EngineObjectDescription initializer;

  private float health;

  private float damage;
  private float rechargeSpeed; // points per seconds (max 100)

  private float speed; // meters per seconds
  private float rotationSpeed; // degree per seconds
  private float turretRotationSpeed; // degree per seconds

  public Characteristic(EngineObjectDescription init)
  {
    initializer = init;
  }

  public Characteristic(Characteristic other)
  {
    initializer = other.initializer;

    health = other.getHealth();

    damage = other.getDamage();
    rechargeSpeed = other.getRechargeSpeed();

    speed = other.getSpeed();
    rotationSpeed = other.getRotationSpeed();
    turretRotationSpeed = other.getTurretRotationSpeed();
  }

  public float getHealth() { return health; }
  public void setHealth(float value) { health = value; }

  public float getRechargeSpeed() { return rechargeSpeed; }
  public void setRechargeSpeed(float value) { rechargeSpeed = value; }

  public float getDamage() { return damage; }
  public void setDamage(float value) { damage = value; }

  public void subtractHealth(float value) { health -= value; }
  public void addHealth(float value) { health += value; }

  public float getSpeed() { return speed; }
  public void setSpeed(float value) { speed = value; }

  public float getRotationSpeed() { return rotationSpeed; }
  public void setRotationSpeed(float value) { rotationSpeed = value; }

  public float getTurretRotationSpeed() { return turretRotationSpeed; }
  public void setTurretRotationSpeed(float value) { turretRotationSpeed = value; }
}
