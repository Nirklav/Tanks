package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;

public class Tank extends GameObject
{
  public static Tank Create(String type)
  {
    Characteristic c = CharacteristicFactory.get(type);
    return new Tank(c);
  }

  protected Tank(Characteristic characteristic) { super(characteristic); }

  public void fire()
  {
    IWorld world = GameContext.getContent().getWorld();
    world.add(Bullet.Create(CharacteristicFactory.BULLET));
  }
}