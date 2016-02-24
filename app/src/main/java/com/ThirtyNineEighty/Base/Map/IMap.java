package com.ThirtyNineEighty.Base.Map;

import com.ThirtyNineEighty.Base.Objects.WorldObject;

public interface IMap
{
  float size();
  IPath findPath(WorldObject<?, ?> finder, WorldObject<?, ?> target);
}
