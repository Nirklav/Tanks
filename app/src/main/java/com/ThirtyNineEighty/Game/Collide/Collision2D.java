package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Helpers.Vector2;

import java.util.Vector;

public class Collision2D
  extends Collision<Vector2>
{
  private Vector2 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision2D(Vector<Vector2> first, Vector<Vector2> second)
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

  private CheckResult check(Vector<Vector2> firstVertices, Vector<Vector2> secondVertices)
  {
    Vector2 mtv = null;
    Vector2 normal = new Vector2();
    float mtvLength = 0.0f;
    boolean IsFirst = true;
    int count = firstVertices.size() + secondVertices.size();

    for (int i = 0; i < count; i += 3)
    {
      setNormal(normal, firstVertices, secondVertices, i);

      Vector2 firstProjection = getProjection(firstVertices, normal);
      Vector2 secondProjection = getProjection(secondVertices, normal);

      if (firstProjection.getX() < secondProjection.getY() || secondProjection.getX() < firstProjection.getY())
        return null;

      if (IsFirst)
      {
        mtv = new Vector2(normal);
        mtvLength = (secondProjection.getY() - firstProjection.getX() > 0)
          ? secondProjection.getY() - firstProjection.getX()
          : firstProjection.getY() - secondProjection.getX();

        IsFirst = false;
      }
      else
      {
        float tempMTVLength = (secondProjection.getY() - firstProjection.getX() > 0)
          ? secondProjection.getY() - firstProjection.getX()
          : firstProjection.getY() - secondProjection.getX();

        if (Math.abs(tempMTVLength) < Math.abs(mtvLength))
        {
          mtv = new Vector2(normal);
          mtvLength = tempMTVLength;
        }
      }
    }

    return new CheckResult(mtv, mtvLength);
  }

  private Vector2 getProjection(Vector<Vector2> vertices, Vector2 normal)
  {
    Vector2 result = null;

    for (Vector2 current : vertices)
    {
      float scalar = current.getScalar(normal);

      if (result == null)
        result = new Vector2(scalar, scalar);

      if (scalar > result.getX())
        result.setX(scalar);

      if (scalar < result.getY())
        result.setY(scalar);
    }

    return result;
  }

  private void setNormal(Vector2 normal, Vector<Vector2> firstVertices, Vector<Vector2> secondVertices, int num)
  {
    if (num < firstVertices.size())
      setNormal(normal, firstVertices, num);
    else
    {
      num -= firstVertices.size();
      setNormal(normal, secondVertices, num);
    }
  }

  private void setNormal(Vector2 normal, Vector<Vector2> vertices, int num)
  {
    Vector2 firstPoint = vertices.get(num);
    Vector2 secondPoint = vertices.get(num + 1 == vertices.size() ? 0 : num + 1);

    Vector2 edge = secondPoint.subtract(firstPoint);

    normal.setX(-edge.getY());
    normal.setY(edge.getX());

    normal.normalize();
  }

  @Override
  public Vector2 getMTV()
  {
    return mtv;
  }

  @Override
  public float getMTVLength()
  {
    return mtvLength;
  }

  @Override
  public boolean isCollide()
  {
    return collide;
  }

  private class CheckResult
  {
    private Vector2 mtv;
    private float mtvLength;

    public CheckResult(Vector2 mtv, float mtvLength)
    {
      this.mtv = mtv;
      this.mtvLength = mtvLength;
    }
  }
}