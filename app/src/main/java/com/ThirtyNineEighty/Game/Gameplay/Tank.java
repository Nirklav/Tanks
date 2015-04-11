package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.GameContext;

public class Tank extends GameObject
{
  private float turretAngle;

  public Tank(String type)
  {
    super(CharacteristicFactory.get(type));
  }

  public void fire()
  {
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

  public void turnTurret(float delta)
  {
    turretAngle += delta;
  }

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