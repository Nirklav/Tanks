package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;

import java.util.ArrayList;

public class Collision3D
{
  private Vector3 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision3D(Collidable first, Collidable second)
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

  private static CheckResult check(Collidable first, Collidable second)
  {
    ArrayList<Vector3> normals = getPlanesNormals(first, second);

    Vector3 mtv = null;
    float minMTVLength = 0;

    for (Vector3 normal : normals)
    {
      Vector2 firstProjection = normal.getProjection(first.getVertices());
      Vector2 secondProjection = normal.getProjection(second.getVertices());

      float mtvLength = getIntersectionLength(firstProjection, secondProjection);
      if (Math.abs(mtvLength) < Vector.epsilon)
        return null;

      if (mtv == null)
      {
        mtv = Vector3.getInstance(normal);
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

    Vector3.release(normals);

    return new CheckResult(mtv, minMTVLength);
  }

  private static ArrayList<Vector3> getPlanesNormals(Collidable first, Collidable second)
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
        result.add(Vector3.getInstance(normal));
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

  public Vector3 getMTV()
  {
    return Vector3.getInstance(mtv);
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
