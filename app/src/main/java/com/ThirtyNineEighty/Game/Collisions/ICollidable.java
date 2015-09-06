package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Common.Math.Vector3;

import java.util.ArrayList;

public interface ICollidable
{
  float getRadius();
  Vector3 getPosition();

  ArrayList<Vector3> getVertices();
  ArrayList<Vector3> getNormals();

  void normalizeLocation();
}
