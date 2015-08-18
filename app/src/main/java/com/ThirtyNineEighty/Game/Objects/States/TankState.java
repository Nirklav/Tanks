package com.ThirtyNineEighty.Game.Objects.States;

public class TankState
  extends GameState
{
  private static final long serialVersionUID = 1L;

  protected float turretAngle;
  protected float rechargeProgress;

  public float getTurretAngle() { return turretAngle; }
  public void setTurretAngle(float value) { turretAngle = value; }

  public float getRechargeProgress() { return rechargeProgress; }
  public void setRechargeProgress(float value) { rechargeProgress = value; }
}
