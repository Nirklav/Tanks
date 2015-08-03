package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Common.Math.Plane;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;

import java.util.ArrayList;

public interface ICollidable
{
  float getRadius();
  Vector3 getPosition();

  ArrayList<Vector2> getConvexHull(Plane plane);
  ArrayList<Vector3> getNormals();

  void normalizeLocation();
}
