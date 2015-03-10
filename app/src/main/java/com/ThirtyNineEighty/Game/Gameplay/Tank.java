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
    Bullet bullet = new Bullet(CharacteristicFactory.BULLET);

    Vector3 bulletPosition = Vector.getInstance(3, position);
    bulletPosition.addToX(100);
    bullet.setPosition(bulletPosition);

    IWorld world = GameContext.getContent().getWorld();
    world.add(bullet);
  }
}