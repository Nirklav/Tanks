package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.RechargeSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.System.*;

public class Tank
  extends GameObject
{
  private float turretAngle;
  private float rechargeProgress;

  public Tank(TankState state)
  {
    super(state);

    turretAngle = state.turretAngle;
    rechargeProgress = state.rechargeProgress;
  }

  public Tank(String type) { this(null, type, new GameProperties()); }
  public Tank(String type, GameProperties properties) { this(null, type, properties); }
  public Tank(String name, String type, GameProperties properties)
  {
    super(name, type, properties);
  }

  @Override
  public void initialize()
  {
    bind(new RechargeSubprogram(this));

    super.initialize();
  }

  @Override
  protected State createState()
  {
    return new TankState();
  }

  @Override
  public State getState()
  {
    TankState state = (TankState) super.getState();
    state.turretAngle = turretAngle;
    state.rechargeProgress = rechargeProgress;
    return state;
  }

  public void fire()
  {
    if (rechargeProgress < GameDescription.maxRechargeLevel)
      return;

    IWorld world = GameContext.content.getWorld();
    rechargeProgress = 0;

    GameProperties properties = getProperties();
    Bullet bullet = new Bullet(properties.getBullet());

    Vector3 bulletAngles = Vector.getInstance(3, angles);
    bulletAngles.addToZ(turretAngle);
    bullet.setAngles(bulletAngles);

    bullet.setPosition(position);
    bullet.move(bullet.collidable.getRadius() + collidable.getRadius());

    world.add(bullet);

    Vector.release(bulletAngles);
  }

  public float getRelativeTurretAngle()
  {
    return turretAngle;
  }

  public float getTurretAngle()
  {
    return Angle.correct(turretAngle + angles.getZ());
  }

  public void addTurretAngle(float delta)
  {
    turretAngle += delta;
  }

  public void turnTurret(float targetAngle)
  {
    GameDescription description = getDescription();
    turretAngle += Angle.getDirection(getTurretAngle(), targetAngle) * description.getTurretRotationSpeed() * GameContext.getDelta();
  }

  public float getRechargeProgress()
  {
    return rechargeProgress;
  }

  public void setRechargeProgress(float value)
  {
    rechargeProgress = value;
  }

  protected static class TankState
    extends GameState
  {
    private static final long serialVersionUID = 1L;

    protected float turretAngle;
    protected float rechargeProgress;
  }
}