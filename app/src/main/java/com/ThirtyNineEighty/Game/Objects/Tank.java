package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.System.*;

public class Tank
  extends GameObject<GameDescription, GameProperties>
{
  private static final long serialVersionUID = 1L;

  protected float turretAngle;
  protected float rechargeProgress;

  public Tank(String descriptionName) { this(descriptionName, new GameProperties()); }
  public Tank(String descriptionName, GameProperties properties)
  {
    super(descriptionName, properties);
  }

  public void fire()
  {
    if (rechargeProgress < GameDescription.maxRechargeLevel)
      return;

    rechargeProgress = 0;

    Vector3 bulletAngles = angles.getSum(0, 0, turretAngle);
    GameProperties properties = getProperties();
    Bullet bullet = new Bullet(properties.getBullet());

    bullet.bind(new MoveSubprogram(bullet, 100));
    bullet.setAngles(bulletAngles);
    bullet.setPosition(position);
    bullet.move(bullet.collidable.getRadius() + collidable.getRadius());

    IWorld world = GameContext.content.getWorld();
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
}