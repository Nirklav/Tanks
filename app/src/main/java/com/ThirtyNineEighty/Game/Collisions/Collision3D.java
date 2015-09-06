package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;

import java.util.ArrayList;

public class Collision3D
{
  private Vector3 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision3D(ICollidable first, ICollidable second)
  {
    CheckResult result = check(first, second);
    if (result == null)
    {
      collide = false;
      return;
    }

    collide = true;
    mtv = result.mtv;
    mtvLength = result.mtvLength;
  }

  private static CheckResult check(ICollidable first, ICollidable second)
  {
    ArrayList<Vector3> normals = getPlanesNormals(first, second);

    Vector3 mtv = null;
    float minMTVLength = 0;

    for (Vector3 normal : normals)
    {
      Vector2 firstProjection = normal.getProjection(first.getVertices());
      Vector2 secondProjection = normal.getProjection(second.getVertices());

      //if (firstProjection.getX() < secondProjection.getY() || secondProjection.getX() < firstProjection.getY())
      //  return null;

      float mtvLength = getIntersectionLength(firstProjection, secondProjection);
      if (Math.abs(mtvLength) < Vector.epsilon)
        return null;

      if (mtv == null)
      {
        mtv = Vector.getInstance(3, normal);
        minMTVLength = mtvLength;
      }
      else
      {
        if (Math.abs(mtvLength) < Math.abs(minMTVLength))
        {
          mtv.setFrom(normal);
          minMTVLength = mtvLength;
        }
      }
    }

    Vector.release(normals);

    return new CheckResult(mtv, minMTVLength);
  }

  private static ArrayList<Vector3> getPlanesNormals(ICollidable first, ICollidable second)
  {
    ArrayList<Vector3> result = new ArrayList<>();
    ArrayList<Vector3> firstNormals = first.getNormals();
    ArrayList<Vector3> secondNormals = second.getNormals();
    int size = firstNormals.size() + secondNormals.size();

    Vector3 normal = new Vector3();

    for(int i = 0; i < size; i++)
    {
      setPlaneNormal(normal, firstNormals, secondNormals, i);

      if (!result.contains(normal))
        result.add((Vector3)Vector.getInstance(3, normal));
    }
    return result;
  }

  private static void setPlaneNormal(Vector3 normal, ArrayList<Vector3> firstNormals, ArrayList<Vector3> secondNormals, int num)
  {
    if (num < firstNormals.size())
      normal.setFrom(firstNormals.get(num));
    else
    {
      num -= firstNormals.size();
      normal.setFrom(secondNormals.get(num));
    }

    normal.normalize();
  }

  private static float getIntersectionLength(Vector2 first, Vector2 second)
  {
    // If swapped result must be negative
    float coeff = 1;

    // Swap (first should be with max X)
    if (first.getX() < second.getX())
    {
      Vector2 temp = first;
      first = second;
      second = temp;
      coeff = -1;
    }

    if (first.getY() < second.getX()) // Intersect
    {
      if (first.getY() > second.getY())
      {
        return coeff * (second.getX() - first.getY());
      }
      else
      {
        float firstResult = coeff * (second.getY() - first.getX());
        float secondResult = coeff * (second.getX() - first.getY());

        if (Math.abs(firstResult) < Math.abs(secondResult))
          return firstResult;
        else
          return secondResult;
      }
    }

    return 0;
  }

//  private static Vector3 getMTV(CheckResult result)
//  {
//    Vector2 mtv2 = result.collision.getMTV();
//    Vector3 mtv3 = new Vector3(mtv2.getX(), mtv2.getY(), 0);
//
//    Vector3 planeX = result.plane.xAxis();
//    Vector3 planeY = result.plane.yAxis();
//    Vector3 planeZ = result.plane.zAxis();
//
//    float[] matrix = new float[16];
//    matrix[0] = planeX.getX();
//    matrix[1] = planeX.getY();
//    matrix[2] = planeX.getZ();
//
//    matrix[4] = planeY.getX();
//    matrix[5] = planeY.getY();
//    matrix[6] = planeY.getZ();
//
//    matrix[8] = planeZ.getX();
//    matrix[9] = planeZ.getY();
//    matrix[10] = planeZ.getZ();
//
//    matrix[15] = 1.0f;
//
//    Matrix.multiplyMV(mtv3.getRaw(), 0, matrix, 0, mtv3.getRaw(), 0);
//
//    mtv3.normalize();
//    return mtv3;
//  }

  public Vector3 getMTV()
  {
    return Vector3.getInstance(3, mtv);
  }

  public float getMTVLength()
  {
    return mtvLength;
  }

  public boolean isCollide()
  {
    return collide;
  }

  @Override
  public String toString()
  {
    return String.format("{Mtv:%s, Length:%f}", mtv == null ? "<null>" : mtv.toString(), mtvLength);
  }

  private static class CheckResult
  {
    private final Vector3 mtv;
    private final float mtvLength;

    public CheckResult(Vector3 mtv, float mtvLength)
    {
      this.mtv = mtv;
      this.mtvLength = mtvLength;
    }
  }
}
