package com.ThirtyNineEighty.Game.Objects.Descriptions;

public class GameDescription
  extends Description
{
  private static final long serialVersionUID = 1L;

  public static final float maxRechargeLevel = 100;

  protected float health;
  protected float damage;
  protected float rechargeSpeed; // points per seconds (max 100)
  protected float speed; // meters per seconds
  protected float rotationSpeed; // degree per seconds
  protected float turretRotationSpeed; // degree per seconds

  public GameDescription(GameDescription other)
  {
    super(other.visuals, other.physical);

    health = other.health;
    damage = other.damage;
    rechargeSpeed = other.rechargeSpeed;
    speed = other.speed;
    rotationSpeed = other.rotationSpeed;
    turretRotationSpeed = other.turretRotationSpeed;
  }

  public float getHealth() { return health; }
  public float getRechargeSpeed() { return rechargeSpeed; }
  public float getDamage() { return damage; }
  public float getSpeed() { return speed; }
  public float getRotationSpeed() { return rotationSpeed; }
  public float getTurretRotationSpeed() { return turretRotationSpeed; }
}
