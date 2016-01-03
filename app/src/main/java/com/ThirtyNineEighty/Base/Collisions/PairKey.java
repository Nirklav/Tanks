package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Objects.WorldObject;

class PairKey
{
  private final Long first;
  private final Long second;

  public PairKey(WorldObject<?, ?> first, WorldObject<?, ?> second)
  {
    Long firstId = first.getId();
    Long secondId = second.getId();

    if (firstId > secondId)
    {
      this.first = firstId;
      this.second = secondId;
    }
    else
    {
      this.first = secondId;
      this.second = firstId;
    }
  }

  @Override
  public int hashCode()
  {
    int hashCode;
    hashCode = first.hashCode();
    hashCode = (hashCode * 397) ^ second.hashCode();
    return hashCode;
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (o instanceof PairKey)
    {
      PairKey key = (PairKey) o;
      return first.equals(key.first) && second.equals(key.second);
    }
    return false;
  }
}
