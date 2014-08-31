package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;

public class Collision2D
  extends Collision
{
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
    float[] firstVertices = figureOne.getVertices();
    float[] secondVertices = figureTwo.getVertices();
    float[] firstNormals = figureOne.getNormals();
    float[] secondNormals = figureTwo.getNormals();

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

  private CheckResult check(float[] firstVertices, float[] secondVertices, float[] normals)
  {
    boolean IsFirst = true;

    int mtvIndex = 0;
    float mtvLength = 0.0f;

    for (int i = 0; i < normals.length; i += 3)
    {
      float[] firstProjection = getProjection(firstVertices, normals, i);
      float[] secondProjection = getProjection(secondVertices, normals, i);

      if (firstProjection[0] < secondProjection[1] || secondProjection[0] < firstProjection[1])
        return null;

      if (IsFirst)
      {
        mtvIndex = i;
        mtvLength = (secondProjection[1] - firstProjection[0] > 0)
          ? secondProjection[1] - firstProjection[0]
          : firstProjection[1] - secondProjection[0];

        IsFirst = false;
      }
      else
      {
        float tempMTVLength = (secondProjection[1] - firstProjection[0] > 0)
          ? secondProjection[1] - firstProjection[0]
          : firstProjection[1] - secondProjection[0];

        if (Math.abs(tempMTVLength) < Math.abs(mtvLength))
        {
          mtvIndex = i;
          mtvLength = tempMTVLength;
        }
      }
    }

    float[] mtv = new float[] { normals[0 + mtvIndex], normals[1 + mtvIndex] };
    return new CheckResult(mtv, mtvLength);
  }

  private float[] getProjection(float[] vertices, float[] normal, int offset)
  {
    float[] result = new float[2];

    result[0] = vertices[0] * normal[0 + offset] + vertices[1] * normal[1 + offset];
    result[1] = result[0];

    for (int i = 1; i < vertices.length; i++)
    {
      float temp = vertices[i] * normal[0 + offset] + vertices[i + 1] * normal[1 + offset];

      if (temp > result[0])
        result[0] = temp;

      if (temp < result[1])
        result[1] = temp;
    }

    return result;
  }

  private class CheckResult
  {
    private float[] mtv;
    private float mtvLength;

    public CheckResult(float[] mtv, float mtvLength)
    {
      this.mtv = mtv;
      this.mtvLength = mtvLength;
    }
  }
}