package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.World;

public class Tank extends GameObject
{
  private double health;

  public Tank(int id, World world)
  {
    super(id, world, "tank");
  }
}
