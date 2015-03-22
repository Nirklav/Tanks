package com.ThirtyNineEighty.Helpers;

public class Plane
{
  private boolean released;

  private Vector3 xAxis;
  private Vector3 yAxis;
  private Vector3 zAxis;

  public Plane()
  {
    xAxis = Vector.getInstance(3, Vector3.xAxis);
    yAxis = Vector.getInstance(3, Vector3.yAxis);
    zAxis = Vector.getInstance(3, Vector3.zAxis);
  }

  public void release()
  {
    Vector.release(xAxis);
    Vector.release(yAxis);
    Vector.release(zAxis);

    released = true;
  }

  public Plane(Vector3 normal)
  {
    this();
    setFrom(normal);
  }

  public Plane(Plane other)
  {
    this();
    setFrom(other);
  }

  public void setFrom(Vector3 normal)
  {
    throwIfReleased();

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
    throwIfReleased();

    xAxis.setFrom(other.xAxis());
    yAxis.setFrom(other.yAxis());
    zAxis.setFrom(other.zAxis());

    xAxis.normalize();
    yAxis.normalize();
    zAxis.normalize();
  }

  public void getProjection(Vector2 result, Vector3 vector)
  {
    throwIfReleased();

    float x = vector.getX() * xAxis.getX() + vector.getY() * xAxis.getY() + vector.getZ() * xAxis.getZ();
    float y = vector.getX() * yAxis.getX() + vector.getY() * yAxis.getY() + vector.getZ() * yAxis.getZ();

    result.setFrom(x, y);
  }

  public Vector2 getProjection(Vector3 vector)
  {
    throwIfReleased();

    Vector2 result = new Vector2();
    getProjection(result, vector);
    return result;
  }

  public void swapZY()
  {
    throwIfReleased();

    Vector3 temp = xAxis;
    xAxis = zAxis;
    zAxis = temp;
  }

  public Vector3 xAxis()
  {
    throwIfReleased();
    return xAxis;
  }

  public Vector3 yAxis()
  {
    throwIfReleased();
    return yAxis;
  }

  public Vector3 zAxis()
  {
    throwIfReleased();
    return zAxis;
  }

  public Vector3 normal()
  {
    throwIfReleased();
    return zAxis;
  }

  public Vector3 getXAxis()
  {
    throwIfReleased();
    return new Vector3(xAxis);
  }

  public Vector3 getYAxis()
  {
    throwIfReleased();
    return new Vector3(yAxis);
  }

  public Vector3 getZAxis()
  {
    throwIfReleased();
    return new Vector3(zAxis);
  }

  public Vector3 getNormal()
  {
    throwIfReleased();
    return new Vector3(zAxis);
  }

  private void throwIfReleased()
  {
    if (released)
      throw new IllegalStateException("plane released do not use it.");
  }
}
