package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;
import com.ThirtyNineEighty.Helpers.VectorUtils;

public class Collision3D
  extends Collision
{
  public Collision3D(IPhysicalObject objectOne, IPhysicalObject objectTwo)
  {
    CheckResult result = check(objectOne, objectTwo);

    if (result == null)
    {
      collide = false;
      return;
    }

    mtv = getMTV3(result);
    mtvLength = result.collision.mtvLength;
  }

  private CheckResult check(IPhysicalObject firstPh, IPhysicalObject secondPh)
  {
    float[] firstNormals = firstPh.getGlobalNormals();
    float[] secondNormals = secondPh.getGlobalNormals();

    CheckResult resultOne = check(firstPh, secondPh, firstNormals);
    if (resultOne == null)
      return null;

    CheckResult resultTwo = check(firstPh, secondPh, secondNormals);
    if (resultTwo == null)
      return null;

    float mtvLengthOne = VectorUtils.getLength2(resultOne.collision.getMTV(), 0);
    float mtvLengthTwo = VectorUtils.getLength2(resultTwo.collision.getMTV(), 0);

    if (mtvLengthOne <= mtvLengthTwo)
      return resultOne;

    return resultTwo;
  }

  private CheckResult check(IPhysicalObject firstPh, IPhysicalObject secondPh, float[] normals)
  {
    Collision2D min = null;
    int minNormalOffset = 0;

    for(int i = 0; i < normals.length; i += 4)
    {
      IPhysicalObject.ConvexHullResult resultOne = firstPh.getConvexHull(normals, i);
      IPhysicalObject.ConvexHullResult resultTwo = secondPh.getConvexHull(normals, i);

      Collision2D collision = new Collision2D(resultOne, resultTwo);
      if (!collision.isCollide())
        return null;

      if (min == null || Collision2D.compare(min, collision) < 0)
      {
        min = collision;
        minNormalOffset = i;
      }
    }

    return new CheckResult(min, normals, minNormalOffset);
  }

  private float[] getMTV3(CheckResult result)
  {
    //TODO : this is not right work

    float[] mtv2 = result.collision.getMTV();
    float[] planeNormals = result.normals;
    int normalOffset = result.normalOffset;

    float[] mtv3 = new float[3];

    float[] axisX = new float[] { -planeNormals[1 + normalOffset], planeNormals[0 + normalOffset], 0f };
    float[] axisY = new float[3];
    VectorUtils.getCross3(axisY, 0, planeNormals, normalOffset, axisX, 0);

    mtv3[0] = mtv2[0] * planeNormals[0 + normalOffset] + mtv2[1] * planeNormals[1 + normalOffset];

    return mtv3;
  }

  private class CheckResult
  {
    public Collision2D collision;
    public float[] normals;
    public int normalOffset;

    public CheckResult(Collision2D collision, float[] normals, int normalOffset)
    {
      this.collision = collision;
      this.normals = normals;
      this.normalOffset = normalOffset;
    }
  }
}
