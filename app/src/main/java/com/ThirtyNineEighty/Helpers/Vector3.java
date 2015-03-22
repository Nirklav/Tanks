package com.ThirtyNineEighty.Helpers;

import android.opengl.Matrix;

/*
 * Operation with prefix get - immutable;
 */
public class Vector3 extends Vector
{
  public final static Vector3 xAxis = new Vector3(1.0f, 0.0f, 0.0f);
  public final static Vector3 yAxis = new Vector3(0.0f, 1.0f, 0.0f);
  public final static Vector3 zAxis = new Vector3(0.0f, 0.0f, 1.0f);
  public final static Vector3 zero = new Vector3(0.0f, 0.0f, 0.0f);

  protected float[] value;

  public Vector3()
  {
    value = new float[4];
  }

  public Vector3(float x, float y, float z)
  {
    this();
    setFrom(x, y, z);
  }

  public Vector3(float[] raw)
  {
    value = raw;
  }

  public Vector3(Vector3 vec)
  {
    this();
    setFrom(vec);
  }

  public void setFrom(float x, float y, float z)
  {
    throwIfReleased();

    value[0] = x;
    value[1] = y;
    value[2] = z;
    value[3] = 1.0f;
  }

  public void setFrom(Vector3 vec)
  {
    throwIfReleased();

    setFrom(vec.getX(), vec.getY(), vec.getZ());
  }

  public float getX() { throwIfReleased(); return value[0]; }
  public float getY() { throwIfReleased(); return value[1]; }
  public float getZ() { throwIfReleased(); return value[2]; }

  public void setX(float v) { throwIfReleased(); value[0] = v; }
  public void setY(float v) { throwIfReleased(); value[1] = v; }
  public void setZ(float v) { throwIfReleased(); value[2] = v; }

  public void addToX(float v) { throwIfReleased(); value[0] += v; }
  public void addToY(float v) { throwIfReleased(); value[1] += v; }
  public void addToZ(float v) { throwIfReleased(); value[2] += v; }

  public float[] getRaw() { throwIfReleased(); return value; }

  public float getLength()
  {
    throwIfReleased();

    double powX = Math.pow(getX(), 2);
    double powY = Math.pow(getY(), 2);
    double powZ = Math.pow(getZ(), 2);

    return (float)Math.sqrt(powX + powY + powZ);
  }

  public float getAngle(Vector3 other)
  {
    throwIfReleased();

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
    throwIfReleased();

    float length = getLength();

    if (length == 0f)
      return;

    value[0] /= length;
    value[1] /= length;
    value[2] /= length;
  }

  public float getScalar(Vector3 other)
  {
    throwIfReleased();

    float multOne   = getX() * other.getX();
    float multTwo   = getY() * other.getY();
    float multThree = getZ() * other.getZ();

    return multOne + multTwo + multThree;
  }

  public void scale(float coefficient)
  {
    throwIfReleased();

    value[0] *= coefficient;
    value[1] *= coefficient;
    value[2] *= coefficient;
  }

  public void cross(Vector3 other)
  {
    throwIfReleased();

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
    throwIfReleased();

    float x = getX();
    float y = getY();
    float z = getZ();

    setFrom(-y, x, 0);

    if (equals(Vector3.zero))
      setFrom(0, z, -y);
  }

  public void add(Vector3 other)
  {
    throwIfReleased();

    setFrom(getX() + other.getX(),
            getY() + other.getY(),
            getZ() + other.getZ());
  }

  public void subtract(Vector3 other)
  {
    throwIfReleased();

    setFrom(getX() - other.getX(),
            getY() - other.getY(),
            getZ() - other.getZ());
  }

  public void multiply(float coefficient)
  {
    throwIfReleased();

    value[0] = value[0] * coefficient;
    value[1] = value[1] * coefficient;
    value[2] = value[2] * coefficient;
  }

  public void move(float length, Vector3 angles)
  {
    throwIfReleased();

    Vector3 vector = new Vector3();
    float[] translateMatrix = new float[16];
    Matrix.setIdentityM(translateMatrix, 0);

    Matrix.rotateM(translateMatrix, 0, angles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(translateMatrix, 0, angles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(translateMatrix, 0, angles.getZ(), 0.0f, 0.0f, 1.0f);

    Matrix.multiplyMV(vector.getRaw(), 0, translateMatrix, 0, Vector3.xAxis.getRaw(), 0);

    vector.normalize();

    value[0] += vector.getX() * length;
    value[1] += vector.getY() * length;
    value[2] += vector.getZ() * length;
  }

  public Vector3 getNormalize()
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.normalize();
    return result;
  }

  public Vector3 getScale(float coefficient)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.scale(coefficient);
    return result;
  }

  public Vector3 getCross(Vector3 other)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.cross(other);
    return result;
  }

  public Vector3 getOrthogonal()
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.orthogonal();
    return result;
  }

  public Vector3 getSum(Vector3 other)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.add(other);
    return result;
  }

  public Vector3 getSubtract(Vector3 other)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.subtract(other);
    return result;
  }

  public Vector3 getMultiply(float coefficient)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.multiply(coefficient);
    return result;
  }

  public Vector3 getMove(float length, Vector3 angles)
  {
    throwIfReleased();

    Vector3 result = new Vector3(this);
    result.move(length, angles);
    return result;
  }

  @Override
  public boolean equals(Object o)
  {
    throwIfReleased();

    Vector3 other = o instanceof Vector3 ? (Vector3)o : null;

    return other != null
      && Math.abs(other.getX() - getX()) < epsilon
      && Math.abs(other.getY() - getY()) < epsilon
      && Math.abs(other.getZ() - getZ()) < epsilon;
  }

  @Override
  public String toString()
  {
    throwIfReleased();
    return String.format("{%f; %f; %f}", value[0], value[1], value[2]);
  }

  @Override
  public float get(int num)
  {
    throwIfReleased();
    return value[num];
  }

  @Override
  public void set(int num, float v)
  {
    throwIfReleased();
    value[num] = v;
  }

  @Override
  public int getSize()
  {
    return 3;
  }
}
