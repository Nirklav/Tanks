package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Subprograms.GameSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Angle;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

public class Tank extends GameObject
{
  private GameSubprogram updateProgram;

  private float turretAngle;
  private float rechargeProgress;

  public Tank(String type)
  {
    super(CharacteristicFactory.get(type));

    updateProgram = new GameSubprogram()
    {
      @Override
      protected void onUpdate()
      {
        Characteristic characteristic = getCharacteristics();

        if (rechargeProgress >= Characteristic.maxRechargeLevel)
          rechargeProgress = Characteristic.maxRechargeLevel;
        else
          rechargeProgress += characteristic.getRechargeSpeed() * GameContext.getDelta();
      }
    };

    IContent content = GameContext.getContent();
    content.bindProgram(updateProgram);
  }

  @Override
  public void onRemoved()
  {
    IContent content = GameContext.getContent();
    content.unbindProgram(updateProgram);
  }

  public void fire()
  {
    if (rechargeProgress < Characteristic.maxRechargeLevel)
      return;

    rechargeProgress = 0;

    Bullet bullet = new Bullet(CharacteristicFactory.BULLET);

    Vector3 bulletAngles = Vector.getInstance(3, angles);
    bulletAngles.addToZ(turretAngle);
    bullet.setAngles(bulletAngles);

    Vector3 bulletPos = Vector.getInstance(3, position);
    float tankRadius = getCollidable().getRadius();
    float bulletRadius = bullet.getCollidable().getRadius();

    bulletPos.move(bulletRadius + tankRadius, bulletAngles);
    bullet.setPosition(bulletPos);

    IWorld world = GameContext.getContent().getWorld();
    world.add(bullet);

    Vector.release(bulletPos);
    Vector.release(bulletAngles);
  }

  public float getTurretAngle() { return Angle.correct(turretAngle + angles.getZ()); }
  public void setTurretAngle(float value) { turretAngle = value; }
  public void addTurretAngle(float delta) { turretAngle += delta; }

  public void turnTurret(float targetAngle)
  {
    Characteristic characteristic = getCharacteristics();
    turretAngle += Angle.getDirection(getTurretAngle(), targetAngle) * characteristic.getTurretRotationSpeed() * GameContext.getDelta();
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
      Vector3 vec = Vector.getInstance(3, angles);
      vec.addToZ(turretAngle);
      renderable.setGlobal(position, vec);
      Vector.release(vec);
      return;
    }

    super.setGlobalRenderablePosition(index, renderable);
  }
}