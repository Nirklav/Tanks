package com.ThirtyNineEighty.Game.Objects.Descriptions;

import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;

import java.util.ArrayList;
import java.util.List;

public class GameDescription
  extends Description
{
  private static final long serialVersionUID = 1L;

  public static final float maxRechargeLevel = 100;

  protected float health;
  protected float damage;
  protected float rechargeSpeed; // points per seconds (max 100)
  protected float turretRotationSpeed; // degree per seconds
  protected ArrayList<String> supportedBullets;
  protected ArrayList<String> supportedUpgrades;

  public GameDescription(String name)
  {
    super(name);
  }

  public float getHealth() { return health; }
  public float getRechargeSpeed() { return rechargeSpeed; }
  public float getDamage() { return damage; }
  public float getTurretRotationSpeed() { return turretRotationSpeed; }
  public List<String> getSupportedBullets() { return supportedBullets; }
  public List<String> getSupportedUpgrades() { return supportedUpgrades; }
}
