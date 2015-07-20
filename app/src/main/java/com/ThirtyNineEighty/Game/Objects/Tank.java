package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Angle;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

public class Tank
  extends GameObject
{
  private float turretAngle;
  private float rechargeProgress;

  public Tank(String type, GameProperties properties)
  {
    super(GameContext.resources.getCharacteristic(new FileDescriptionSource(type)), properties);
  }

  @Override
  public void initialize()
  {
    super.initialize();

    bindProgram(new Subprogram()
    {
      @Override
      protected void onUpdate()
      {
        GameDescription description = getDescription();

        if (rechargeProgress >= GameDescription.maxRechargeLevel)
          rechargeProgress = GameDescription.maxRechargeLevel;
        else
          rechargeProgress += description.getRechargeSpeed() * GameContext.getDelta();
      }
    });
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

  //TODO implement true interface for multi models objects
  @Override
  protected void setGlobalRenderablePosition(int index, I3DRenderable renderable)
  {
    if (index == 1) // turret model
    {
      Vector3 turretAngles = Vector.getInstance(3);
      turretAngles.addToZ(turretAngle);
      renderable.setLocal(Vector3.zero, turretAngles);
    }

    super.setGlobalRenderablePosition(index, renderable);
  }
}