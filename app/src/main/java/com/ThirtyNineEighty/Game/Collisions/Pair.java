package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Objects.EngineObject;

class Pair
{
  public final EngineObject first;
  public final EngineObject second;

  public boolean firstMoved;
  public boolean secondMoved;
  public Collision3D collision;

  public Pair(EngineObject first, EngineObject second)
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