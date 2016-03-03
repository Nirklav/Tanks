package com.ThirtyNineEighty.Base.Common.Math;

public class Plane
{
  public static final Plane zero = new Plane(new Vector3(0, 0, 0));

  private boolean released;

  private Vector3 xAxis;
  private Vector3 yAxis;
  private Vector3 zAxis;

  public Plane()
  {
    this(Vector3.xAxis, Vector3.yAxis, Vector3.zAxis);
  }

  public Plane(Vector3 x, Vector3 y, Vector3 z)
  {
    xAxis = Vector3.getInstance(x);
    yAxis = Vector3.getInstance(y);
    zAxis = Vector3.getInstance(z);
  }

  public void release()
  {
    released = true;

    Vector3.release(xAxis);
    Vector3.release(yAxis);
    Vector3.release(zAxis);
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

  public void swapXZ()
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
    return Vector3.getInstance(xAxis);
  }

  public Vector3 getYAxis()
  {
    throwIfReleased();
    return Vector3.getInstance(yAxis);
  }

  public Vector3 getZAxis()
  {
    throwIfReleased();
    return Vector3.getInstance(zAxis);
  }

  public Vector3 getNormal()
  {
    throwIfReleased();
    return Vector3.getInstance(zAxis);
  }

  private void throwIfReleased()
  {
    if (released)
      throw new IllegalStateException("plane released do not use it.");
  }

  @Override
  public boolean equals(Object o)
  {
    throwIfReleased();

    Plane other = o instanceof Plane ? (Plane)o : null;

    return other != null
      && xAxis.equals(other.xAxis)
      && yAxis.equals(other.yAxis)
      && zAxis.equals(other.zAxis);
  }

  @Override
  public String toString()
  {
    throwIfReleased();
    return String.format("[x:%s, y:%s, z:%s]", xAxis.toString(), yAxis.toString(), zAxis.toString());
  }
}
