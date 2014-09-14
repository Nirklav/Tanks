package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.Vector;

public interface IPhysicalObject
{
  void setGlobal(Vector3 position, float angleX, float angleY, float angleZ);

  Vector<Vector2> getConvexHull(Plane plane);
  Vector<Vector3> getGlobalVertices();
  Vector<Vector3> getGlobalNormals();
}
