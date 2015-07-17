package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.ArrayList;

public interface ICollidable
{
  float getRadius();
  Vector3 getPosition();
  Vector3 getAngles();

  void setGlobal(Vector3 position, Vector3 angles);

  ArrayList<Vector2> getConvexHull(Plane plane);
  ArrayList<Vector3> getNormals();
}
