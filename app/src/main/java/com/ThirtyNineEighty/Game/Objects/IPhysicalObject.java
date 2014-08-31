package com.ThirtyNineEighty.Game.Objects;

public interface IPhysicalObject
{
  void setGlobal(float[] position, float angleX, float angleY, float angleZ);

  ConvexHullResult getConvexHull(float[] planeNormal, int offset);
  float[] getGlobalNormals();

  public class ConvexHullResult
  {
    private float[] vertices;
    private float[] normals;

    public ConvexHullResult(float[] vertices, float[] normals)
    {
      this.vertices = vertices;
      this.normals = normals;
    }

    public float[] getVertices()
    {
      return vertices;
    }

    public float[] getNormals()
    {
      return normals;
    }
  }
}
