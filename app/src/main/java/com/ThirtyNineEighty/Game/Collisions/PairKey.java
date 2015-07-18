package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Objects.EngineObject;

class PairKey
{
  private final String first;
  private final String second;

  public PairKey(EngineObject first, EngineObject second)
  {
    String firstName = first.getName();
    String secondName = second.getName();

    if (firstName.compareTo(secondName) < 0)
    {
      this.first = firstName;
      this.second = secondName;
    }
    else
    {
      this.first = secondName;
      this.second = firstName;
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
