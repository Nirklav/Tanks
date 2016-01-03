package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Objects.WorldObject;

class Pair
{
  public final WorldObject<?, ?> first;
  public final WorldObject<?, ?> second;

  public boolean firstMoved;
  public boolean secondMoved;
  public Collision3D collision;

  public Pair(WorldObject<?, ?> first, WorldObject<?, ?> second)
  {
    Long firstId = first.getId();
    Long secondId = second.getId();

    if (firstId > secondId)
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