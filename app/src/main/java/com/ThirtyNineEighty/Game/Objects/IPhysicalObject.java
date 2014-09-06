package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.Vector;

public interface IPhysicalObject
{
  void setGlobal(Vector3 position, float angleX, float angleY, float angleZ);

  ConvexHullResult getConvexHull(Vector3 planeNormal);
  Vector<Vector3> getGlobalNormals();

  public class ConvexHullResult
  {
    private Vector<Vector2> vertices;
    private Vector<Vector2> normals;

    public ConvexHullResult(Vector<Vector2> vertices, Vector<Vector2> normals)
    {
      this.vertices = vertices;
      this.normals = normals;
    }

    public Vector<Vector2> getVertices()
    {
      return vertices;
    }

    public Vector<Vector2> getNormals()
    {
      return normals;
    }
  }
}
