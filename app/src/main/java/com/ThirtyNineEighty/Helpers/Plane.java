package com.ThirtyNineEighty.Helpers;

public class Plane
{
  private Vector3 xAxis;
  private Vector3 yAxis;
  private Vector3 zAxis;

  // <editor-fold desc="initialization">

  public Plane()
  {
    xAxis = new Vector3(Vector3.xAxis);
    yAxis = new Vector3(Vector3.yAxis);
    zAxis = new Vector3(Vector3.zAxis);
  }

  public Plane(Vector3 normal)
  {
    zAxis = new Vector3(normal);
    xAxis = normal.getOrthogonal();
    yAxis = normal.getCross(xAxis);

    xAxis.normalize();
    yAxis.normalize();
    zAxis.normalize();
  }

  public void setFrom(Vector3 normal)
  {
    zAxis.setFrom(normal);

    xAxis.setFrom(normal);
    xAxis.orthogonal();

    yAxis.setFrom(normal);
    yAxis.cross(xAxis);

    xAxis.normalize();
    yAxis.normalize();
    zAxis.normalize();
  }

  public void setFrom(Plane other)
  {
    xAxis.setFrom(other.xAxis());
    yAxis.setFrom(other.yAxis());
    zAxis.setFrom(other.zAxis());

    xAxis.normalize();
    yAxis.normalize();
    zAxis.normalize();
  }

  // </editor-fold>

  public void getProjection(Vector2 result, Vector3 vector)
  {
    float x = vector.getX() * xAxis.getX() + vector.getY() * xAxis.getY() + vector.getZ() * xAxis.getZ();
    float y = vector.getX() * yAxis.getX() + vector.getY() * yAxis.getY() + vector.getZ() * yAxis.getZ();

    result.setFrom(x, y);
  }

  public Vector2 getProjection(Vector3 vector)
  {
    Vector2 result = new Vector2();
    getProjection(result, vector);
    return result;
  }

  // <editor-fold desc="getters">

  public Vector3 xAxis()
  {
    return xAxis;
  }

  public Vector3 yAxis()
  {
    return yAxis;
  }

  public Vector3 zAxis()
  {
    return zAxis;
  }

  public Vector3 normal()
  {
    return zAxis;
  }

  public Vector3 getXAxis()
  {
    return new Vector3(xAxis);
  }

  public Vector3 getYAxis()
  {
    return new Vector3(yAxis);
  }

  public Vector3 getZAxis()
  {
    return new Vector3(zAxis);
  }

  public Vector3 getNormal()
  {
    return new Vector3(zAxis);
  }

  // </editor-fold>
}
