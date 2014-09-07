package com.ThirtyNineEighty.Helpers;

public class Vector3
{
  public static Vector3 xAxis = new Vector3(1.0f, 0.0f, 0.0f);
  public static Vector3 yAxis = new Vector3(0.0f, 1.0f, 0.0f);
  public static Vector3 zAxis = new Vector3(0.0f, 0.0f, 1.0f);

  protected float[] value;

  public Vector3()
  {
    this(0, 0, 0);
  }

  public Vector3(float x, float y, float z)
  {
    value = new float[4];
    value[0] = x;
    value[1] = y;
    value[2] = z;
  }

  public Vector3(float[] raw)
  {
    if (raw.length == 4)
      value = raw;
    else
    {
      value = new float[4];
      value[0] = raw[0];
      value[1] = raw[1];
      value[2] = raw[2];
    }
  }

  public Vector3(Vector3 vec)
  {
    this(vec.getX(), vec.getY(), vec.getZ());
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

  public void normalize()
  {
    float length = getLength();

    value[0] /= length;
    value[1] /= length;
    value[2] /= length;
  }

  public float getAngle(Vector3 other)
  {
    float scalar = getScalar(other);
    float lengthOne = this.getLength();
    float lengthTwo = other.getLength();

    return (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));
  }

  public float getScalar(Vector3 other)
  {
    float multOne   = getX() * other.getX();
    float multTwo   = getY() * other.getY();
    float multThree = getZ() * other.getZ();

    return multOne + multTwo + multThree;
  }

  public Vector3 getCross(Vector3 other)
  {
    float[] otherValue = other.getRaw();

    float result1 =      value[1] * otherValue[2];
    float result2 = -1 * value[2] * otherValue[1];
    float result3 = -1 * value[0] * otherValue[2];
    float result4 =      value[2] * otherValue[0];
    float result5 =      value[0] * otherValue[1];
    float result6 = -1 * value[1] * otherValue[0];

    return new Vector3(result1 + result2, result3 + result4, result5 + result6);
  }

  public Vector3 subtract(Vector3 other)
  {
    return new Vector3
    (
      getX() - other.getX(),
      getY() - other.getY(),
      getZ() - other.getZ()
    );
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
}
