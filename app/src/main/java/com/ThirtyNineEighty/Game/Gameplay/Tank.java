package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
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
    IWorld world = GameContext.getContent().getWorld();
    world.add(bullet);
  }
}