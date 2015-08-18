package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Objects.WorldObject;

class Pair
{
  public final WorldObject first;
  public final WorldObject second;

  public boolean firstMoved;
  public boolean secondMoved;
  public Collision3D collision;

  public Pair(WorldObject first, WorldObject second)
  {
    String firstName = first.getName();
    String secondName = second.getName();

    if (firstName.compareTo(secondName) < 0)
    {
      this.first = first;
      this.second = second;
    }
    else
    {
      this.first = second;
      this.second = first;
    }
  }
}