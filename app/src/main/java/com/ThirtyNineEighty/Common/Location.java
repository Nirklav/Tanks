package com.ThirtyNineEighty.Common;

import com.ThirtyNineEighty.Common.Math.Vector;

public class Location<T extends Vector>
{
  public T position;
  public T angles;

  public T localPosition;
  public T localAngles;

  public Location() { }

  public Location(int vecSize)
  {
    position = Vector.getInstance(vecSize);
    angles = Vector.getInstance(vecSize);
    localAngles = Vector.getInstance(vecSize);
    localPosition = Vector.getInstance(vecSize);
  }

  @Override
  public boolean equals(Object o)
  {
    Location<?> loc = o instanceof Location<?> ? (Location<?>)o : null;

    return loc != null
        && position.equals(loc.position)
        && angles.equals(loc.angles)
        && localPosition.equals(loc.localPosition)
        && localAngles.equals(loc.localAngles);
  }

  @Override
  public int hashCode()
  {
    int hashCode = 0;
    hashCode = (hashCode * 397) ^ position.hashCode();
    hashCode = (hashCode * 397) ^ angles.hashCode();
    hashCode = (hashCode * 397) ^ localPosition.hashCode();
    hashCode = (hashCode * 397) ^ localAngles.hashCode();
    return hashCode;
  }
}
