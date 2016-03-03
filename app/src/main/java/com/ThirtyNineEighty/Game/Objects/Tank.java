package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Game.TanksContext;

public class Tank
  extends GameObject<GameDescription, GameProperties>
{
  private static final long serialVersionUID = 1L;

  public static final String EventFire = "Tank_Fire";

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

    Bullet bullet = new Bullet(properties.getBullet());
    IWorld world = TanksContext.content.getWorld();
    world.add(bullet);

    bullet.bind(new MoveSubprogram(bullet, 100));
    bullet.setAngles(bulletAngles);
    bullet.setPosition(position);
    bullet.move(bullet.collidable.getRadius() + collidable.getRadius());

    Vector3.release(bulletAngles);

    enqueueViewEvent(EventFire);
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
    float direction = Angle.getDirection(getTurretAngle(), targetAngle);
    float speed = description.getTurretRotationSpeed();

    turretAngle += direction * speed * DeltaTime.get();
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