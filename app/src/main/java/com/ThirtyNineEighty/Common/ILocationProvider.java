package com.ThirtyNineEighty.Common;

import com.ThirtyNineEighty.Common.Math.Vector;

public interface ILocationProvider<T extends Vector>
{
  Location<T> getLocation();
}
