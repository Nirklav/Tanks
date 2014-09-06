package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;
import com.ThirtyNineEighty.Helpers.Vector2;

import java.util.Vector;

public class Collision2D
  extends Collision<Vector2>
{
  private Vector2 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision2D(IPhysicalObject.ConvexHullResult first, IPhysicalObject.ConvexHullResult second)
  {
    CheckResult result = check(first, second);

    if (result == null)
    {
      collide = false;
      return;
    }

    mtv = result.mtv;
    mtvLength = result.mtvLength;
  }

  private CheckResult check(IPhysicalObject.ConvexHullResult figureOne, IPhysicalObject.ConvexHullResult figureTwo)
  {
    Vector<Vector2> firstVertices = figureOne.getVertices();
    Vector<Vector2> secondVertices = figureTwo.getVertices();
    Vector<Vector2> firstNormals = figureOne.getNormals();
    Vector<Vector2> secondNormals = figureTwo.getNormals();

    CheckResult firstResult = check(firstVertices, secondVertices, firstNormals);
    if (firstResult == null)
      return null;

    CheckResult secondResult = check(firstVertices, secondVertices, secondNormals);
    if (secondResult == null)
      return null;

    if (firstResult.mtvLength <= secondResult.mtvLength)
      return firstResult;

    return secondResult;
  }

  private CheckResult check(Vector<Vector2> firstVertices, Vector<Vector2> secondVertices, Vector<Vector2> normals)
  {
    boolean IsFirst = true;

    int mtvIndex = 0;
    float mtvLength = 0.0f;

    for (int i = 0; i < normals.size(); i += 3)
    {
      Vector2 normal = normals.get(i);
      Vector2 firstProjection = getProjection(firstVertices, normal);
      Vector2 secondProjection = getProjection(secondVertices, normal);

      if (firstProjection.getX() < secondProjection.getY() || secondProjection.getX() < firstProjection.getY())
        return null;

      if (IsFirst)
      {
        mtvIndex = i;
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
          mtvIndex = i;
          mtvLength = tempMTVLength;
        }
      }
    }

    return new CheckResult(normals.get(mtvIndex), mtvLength);
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