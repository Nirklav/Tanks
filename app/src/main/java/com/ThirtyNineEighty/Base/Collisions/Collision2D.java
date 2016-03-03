package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;

import java.util.ArrayList;

public class Collision2D
{
  private Vector2 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision2D(ArrayList<Vector2> first, ArrayList<Vector2> second)
  {
    CheckResult result = check(first, second);

    if (result == null)
    {
      collide = false;
      return;
    }

    collide = true;
    mtv = result.mtv;
    mtvLength = Math.abs(result.mtvLength);
  }

  private static CheckResult check(ArrayList<Vector2> firstVertices, ArrayList<Vector2> secondVertices)
  {
    Vector2 mtv = null;
    Vector2 normal = Vector2.getInstance();
    float minMTVLength = 0;
    int count = firstVertices.size() + secondVertices.size();

    for (int i = 0; i < count; i ++)
    {
      setNormal(normal, firstVertices, secondVertices, i);

      Vector2 firstProjection = normal.getProjection(firstVertices);
      Vector2 secondProjection = normal.getProjection(secondVertices);

      if (firstProjection.getX() < secondProjection.getY() || secondProjection.getX() < firstProjection.getY())
        return null;

      if (mtv == null)
      {
        mtv = Vector2.getInstance(normal);
        minMTVLength = getIntersectionLength(firstProjection, secondProjection);
      }
      else
      {
        float mtvLength = getIntersectionLength(firstProjection, secondProjection);
        if (Math.abs(mtvLength) < Math.abs(minMTVLength))
        {
          mtv.setFrom(normal);
          minMTVLength = mtvLength;
        }
      }
    }

    return new CheckResult(mtv, minMTVLength);
  }

  private static float getIntersectionLength(Vector2 firstProjection, Vector2 secondProjection)
  {
    return (secondProjection.getY() - firstProjection.getX() > 0)
      ? secondProjection.getY() - firstProjection.getX()
      : firstProjection.getY() - secondProjection.getX();
  }

  private static void setNormal(Vector2 normal, ArrayList<Vector2> firstVertices, ArrayList<Vector2> secondVertices, int num)
  {
    if (num < firstVertices.size())
      setNormal(normal, firstVertices, num);
    else
    {
      num -= firstVertices.size();
      setNormal(normal, secondVertices, num);
    }
  }

  private static void setNormal(Vector2 normal, ArrayList<Vector2> vertices, int num)
  {
    Vector2 firstPoint = vertices.get(num);
    Vector2 secondPoint = vertices.get(num + 1 == vertices.size() ? 0 : num + 1);

    Vector2 edge = secondPoint.getSubtract(firstPoint);

    normal.setX(-edge.getY());
    normal.setY(edge.getX());

    normal.normalize();
  }

  public Vector2 getMTV()
  {
    return Vector2.getInstance(mtv);
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
    private final Vector2 mtv;
    private final float mtvLength;

    public CheckResult(Vector2 mtv, float mtvLength)
    {
      this.mtv = mtv;
      this.mtvLength = mtvLength;
    }
  }
}