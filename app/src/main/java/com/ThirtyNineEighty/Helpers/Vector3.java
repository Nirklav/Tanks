package com.ThirtyNineEighty.Helpers;

/*
 * Operation with prefix get - immutable;
 */
public class Vector3 extends Vector
{
  public static Vector3 xAxis = new Vector3(1.0f, 0.0f, 0.0f);
  public static Vector3 yAxis = new Vector3(0.0f, 1.0f, 0.0f);
  public static Vector3 zAxis = new Vector3(0.0f, 0.0f, 1.0f);
  public static Vector3 zero = new Vector3(0.0f, 0.0f, 0.0f);

  protected float[] value;

  public Vector3()
  {
    this(0, 0, 0);
  }

  public Vector3(float x, float y, float z)
  {
    value = new float[4];
    setFrom(x, y, z);
  }

  public Vector3(float[] raw)
  {
    value = raw;
  }

  public Vector3(Vector3 vec)
  {
    value = new float[4];
    setFrom(vec.getX(), vec.getY(), vec.getZ());
  }

  public void setFrom(float x, float y, float z)
  {
    value[0] = x;
    value[1] = y;
    value[2] = z;
    value[3] = 1.0f;
  }

  public void setFrom(Vector3 vec)
  {
    setFrom(vec.getX(), vec.getY(), vec.getZ());
  }

  public float getX() { return value[0]; }
  public float getY() { return value[1]; }
  public float getZ() { return value[2]; }

  public void setX(float v) { value[0] = v; }
  public void setY(float v) { value[1] = v; }
  public void setZ(float v) { value[2] = v; }

  public void addToX(float v) { value[0] += v; }
  public void addToY(float v) { value[1] += v; }
  public void addToZ(float v) { value[2] += v; }

  public float[] getRaw() { return value; }

  public float getLength()
  {
    double powX = Math.pow(getX(), 2);
    double powY = Math.pow(getY(), 2);
    double powZ = Math.pow(getZ(), 2);

    return (float)Math.sqrt(powX + powY + powZ);
  }

  public float getAngle(Vector3 other)
  {
    Vector3 normal = getCross(other);

    if (normal.equals(Vector3.zero))
      return getScalar(other) > 0 ? 0 : 180;

    Plane plane = new Plane(normal);

    Vector2 vecOne = plane.getProjection(this);
    Vector2 vecTwo = plane.getProjection(other);

    return vecOne.getAngle(vecTwo);
  }

  public void normalize()
  {
    float length = getLength();

    if (length == 0f)
      return;

    value[0] /= length;
    value[1] /= length;
    value[2] /= length;
  }

  public float getScalar(Vector3 other)
  {
    float multOne   = getX() * other.getX();
    float multTwo   = getY() * other.getY();
    float multThree = getZ() * other.getZ();

    return multOne + multTwo + multThree;
  }

  public void scale(float coefficient)
  {
    value[0] *= coefficient;
    value[1] *= coefficient;
    value[2] *= coefficient;
  }

  public void cross(Vector3 other)
  {
    float[] otherValue = other.getRaw();

    float result1 =      value[1] * otherValue[2];
    float result2 = -1 * value[2] * otherValue[1];
    float result3 = -1 * value[0] * otherValue[2];
    float result4 =      value[2] * otherValue[0];
    float result5 =      value[0] * otherValue[1];
    float result6 = -1 * value[1] * otherValue[0];

    setFrom(result1 + result2, result3 + result4, result5 + result6);
  }

  public void orthogonal()
  {
    float x = getX();
    float y = getY();
    float z = getZ();

    setFrom(-y, x, 0);

    if (this.equals(Vector3.zero))
      setFrom(0, z, -y);
  }

  public void subtract(Vector3 other)
  {
    setFrom(getX() - other.getX(),
            getY() - other.getY(),
            getZ() - other.getZ());
  }

  public Vector3 getNormalize()
  {
    Vector3 result = new Vector3(this);
    result.normalize();
    return result;
  }

  public Vector3 getScale(float coefficient)
  {
    Vector3 result = new Vector3(this);
    result.scale(coefficient);
    return result;
  }

  public Vector3 getCross(Vector3 other)
  {
    Vector3 result = new Vector3(this);
    result.cross(other);
    return result;
  }

  public Vector3 getOrthogonal()
  {
    Vector3 result = new Vector3(this);
    result.orthogonal();
    return result;
  }

  public Vector3 getSubtract(Vector3 other)
  {
    Vector3 result = new Vector3(this);
    result.subtract(other);
    return result;
  }

  @Override
  public boolean equals(Object o)
  {
    Vector3 other = o instanceof Vector3 ? (Vector3)o : null;

    return other != null
      && other.getX() == getX()
      && other.getY() == getY()
      && other.getZ() == getZ();
  }

  @Override
  public String toString()
  {
    return String.format("{%f; %f; %f}", value[0], value[1], value[2]);
  }

  @Override
  public float get(int num)
  {
    return value[num];
  }

  @Override
  public void set(int num, float v)
  {
    value[num] = v;
  }
}
