package com.ThirtyNineEighty.Base.Common.Math;

import android.opengl.Matrix;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/*
 * Operation with prefix get - immutable;
 */
public class Vector2
  extends Vector
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final static Vector2 xAxis = new Vector2(1.0f, 0.0f);
  public final static Vector2 yAxis = new Vector2(0.0f, 1.0f);
  public final static int size = 2;

  public static final VectorsPool<Vector2> pool = new VectorsPool<>("Vector2", poolLimit);

  public static Vector2 getInstance(Vector other)
  {
    Vector2 vector = getInstance();
    vector.setFrom(other);
    return vector;
  }

  public static Vector2 getInstance(float... values)
  {
    Vector2 vector = getInstance();
    System.arraycopy(values, 0, vector.value, 0, values.length);
    return vector;
  }

  public static Vector2 getInstance(ByteBuffer dataBuffer)
  {
    Vector2 vector = getInstance();
    for (int i = 0; i < size; i++)
      vector.value[i] = dataBuffer.getFloat();
    return vector;
  }

  public static Vector2 getInstance()
  {
    Vector2 vector = pool.acquire();
    if (vector == null)
      vector = new Vector2();
    return vector;
  }

  public static void release(Vector2 vector)
  {
    pool.release(vector);
  }

  public static void release(Collection<Vector2> vectors)
  {
    pool.release(vectors);
  }

  public Vector2()
  {
    value = new float[2];
  }

  public Vector2(float x, float y)
  {
    this();
    setFrom(x, y);
  }

  public Vector2(float[] raw)
  {
    value = raw;
  }

  public Vector2(Vector2 vec)
  {
    this();
    setFrom(vec);
  }

  public void setFrom(float x, float y)
  {
    throwIfReleased();

    value[0] = x;
    value[1] = y;
  }

  public void setFrom(Vector2 vec)
  {
    throwIfReleased();

    setFrom(vec.getX(), vec.getY());
  }

  public float getX() { throwIfReleased(); return value[0]; }
  public float getY() { throwIfReleased(); return value[1]; }

  public void setX(float v) { throwIfReleased(); value[0] = v; }
  public void setY(float v) { throwIfReleased(); value[1] = v; }

  public void addToX(float v) { throwIfReleased(); value[0] += v; }
  public void addToY(float v) { throwIfReleased(); value[1] += v; }

  public void multiplyToX(float v) { throwIfReleased(); value[0] *= v; }
  public void multiplyToY(float v) { throwIfReleased(); value[1] *= v; }

  public float getLength()
  {
    throwIfReleased();

    double powX = Math.pow(getX(), 2);
    double powY = Math.pow(getY(), 2);

    return (float)Math.sqrt(powX + powY);
  }

  public float getAngle(Vector2 other)
  {
    throwIfReleased();

    float scalar = getScalar(other);
    float lengthOne = this.getLength();
    float lengthTwo = other.getLength();
    float angle = (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));

    return Angle.correct(getCross(other) > 0 ? angle : 360 - angle);
  }

  public float getScalar(Vector2 other)
  {
    throwIfReleased();

    float multOne = getX() * other.getX();
    float multTwo = getY() * other.getY();

    return multOne + multTwo;
  }

  public float getCross(Vector2 other)
  {
    throwIfReleased();

    return getX() * other.getY() - getY() * other.getX();
  }

  public void normalize()
  {
    throwIfReleased();

    float length = getLength();

    value[0] /= length;
    value[1] /= length;
  }

  public void rotate(float angle)
  {
    throwIfReleased();

    double radians = Math.toRadians(angle);
    double ca = Math.cos(radians);
    double sa = Math.sin(radians);

    value[0] = (float) (ca * value[0] - sa * value[1]);
    value[1] = (float) (sa * value[0] + ca * value[1]);
  }

  public void subtract(Vector2 other)
  {
    throwIfReleased();

    setFrom(getX() - other.getX(), getY() - other.getY());
  }

  public void add(Vector2 other)
  {
    throwIfReleased();

    setFrom(getX() + other.getX(), getY() + other.getY());
  }

  public void multiply(float coefficient)
  {
    throwIfReleased();

    value[0] = value[0] * coefficient;
    value[1] = value[1] * coefficient;
  }

  public Vector3 toVector3(Plane plane)
  {
    Vector3 mtv3 = new Vector3(getX(), getY(), 0);

    Vector3 planeX = plane.xAxis();
    Vector3 planeY = plane.yAxis();
    Vector3 planeZ = plane.zAxis();

    float[] matrix = new float[16];
    matrix[0] = planeX.getX();
    matrix[1] = planeX.getY();
    matrix[2] = planeX.getZ();

    matrix[4] = planeY.getX();
    matrix[5] = planeY.getY();
    matrix[6] = planeY.getZ();

    matrix[8] = planeZ.getX();
    matrix[9] = planeZ.getY();
    matrix[10] = planeZ.getZ();

    matrix[15] = 1.0f;

    Matrix.multiplyMV(mtv3.getRaw(), 0, matrix, 0, mtv3.getRaw(), 0);

    mtv3.normalize();
    return mtv3;
  }

  public void lineProjection(Vector2 start, Vector2 end)
  {
    // a * dot(a, b) / dot(a,a)
    Vector2 lineVector = end.getSubtract(start);
    subtract(start);

    float crossProduct = getCross(lineVector);

    float first = getScalar(lineVector);
    float second = lineVector.getScalar(lineVector);

    setFrom(lineVector);
    multiply(first / second);

    if (crossProduct < 0)
      multiply(-1);

    add(start);

    Vector2.release(lineVector);
  }

  public Vector2 getNormalize()
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.normalize();
    return result;
  }

  public Vector2 getRotated(float angles)
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.rotate(angles);
    return result;
  }

  public void move(float length, float angle)
  {
    throwIfReleased();

    value[0] += length * Math.cos(Math.toRadians(angle));
    value[1] += length * Math.sin(Math.toRadians(angle));
  }

  public Vector2 getSubtract(Vector2 other)
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.subtract(other);
    return result;
  }

  public Vector2 getSum(Vector2 other)
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.add(other);
    return result;
  }

  public Vector2 getMultiply(float coefficient)
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.multiply(coefficient);
    return result;
  }

  public Vector2 getMove(float length, float angle)
  {
    throwIfReleased();

    Vector2 result = getInstance(this);
    result.move(length, angle);
    return result;
  }

  public Vector2 getProjection(ArrayList<Vector2> vertices)
  {
    Vector2 result = null;

    for (Vector2 current : vertices)
    {
      float projection = getScalar(current);

      if (result == null)
        result = getInstance(projection, projection);

      // x - max
      if (projection > result.getX())
        result.setX(projection);

      // y - min
      if (projection < result.getY())
        result.setY(projection);
    }

    return result;
  }

  @Override
  public int getSize()
  {
    return size;
  }

  @Override
  public void clear()
  {
    value[0] = 0;
    value[1] = 0;
  }

  @Override
  public boolean equals(Object o)
  {
    throwIfReleased();

    Vector2 other = o instanceof Vector2 ? (Vector2)o : null;

    return other != null
      && Math.abs(other.getX() - getX()) < epsilon
      && Math.abs(other.getY() - getY()) < epsilon;
  }

  @Override
  public int hashCode()
  {
    throwIfReleased();
    return Arrays.hashCode(value);
  }

  @Override
  public String toString()
  {
    throwIfReleased();
    return String.format("{%f; %f}", value[0], value[1]);
  }
}
