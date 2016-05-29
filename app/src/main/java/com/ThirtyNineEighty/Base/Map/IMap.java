package com.ThirtyNineEighty.Base.Map;

import com.ThirtyNineEighty.Base.IStatistics;
import com.ThirtyNineEighty.Base.Objects.WorldObject;

public interface IMap
  extends IStatistics
{
  float size();
  IPath findPath(WorldObject<?, ?> finder, WorldObject<?, ?> target);
}
