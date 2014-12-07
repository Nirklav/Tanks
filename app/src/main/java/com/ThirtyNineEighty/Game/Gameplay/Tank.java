package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Worlds.IGameWorld;

public class Tank extends GameObject
{
  public Tank(IGameWorld world)
  {
    super(CharacteristicFactory.TANK, world, "tank");
  }
}
