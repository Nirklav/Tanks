package com.ThirtyNineEighty.Base.Map;

import com.ThirtyNineEighty.Base.Objects.WorldObject;

public interface IMap
{
  float size();
  boolean canMove(WorldObject<?, ?> object);
  IPath findPath(WorldObject<?, ?> finder, WorldObject<?, ?> target);
}
