package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

public class Tank extends GameObject
{
  public Tank(String type)
  {
    super(CharacteristicFactory.get(type));
  }

  public void fire()
  {
    Vector3 angles = getAngles();

    Bullet bullet = new Bullet(CharacteristicFactory.BULLET);
    Vector3 bulletPos = Vector.getInstance(3, getPosition());
    bulletPos.move(getCollidable().getRadius(), angles);

    bullet.setPosition(bulletPos);
    bullet.setAngles(angles);

    IWorld world = GameContext.getContent().getWorld();
    world.add(bullet);
  }
}