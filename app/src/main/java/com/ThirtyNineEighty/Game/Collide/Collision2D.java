package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;
import com.ThirtyNineEighty.Helpers.VectorUtils;

public class Collision2D
  extends Collision
{
  public Collision2D(IPhysicalObject.ConvexHullResult first, IPhysicalObject.ConvexHullResult second)
  {

  }

  private void verifyCollision(IPhysicalObject.ConvexHullResult figureOne, IPhysicalObject.ConvexHullResult figureTwo)
  {

  }

  private float[] getProjectionOnLine(float[] vertices, float[] normal, int offset)
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

  private float[] selectMinMTV(float[] currentMTV3, float[] checkedMTV3)
  {
    float lengthCurrent = VectorUtils.getLength2(currentMTV3, 0);
    float lengthChecked = VectorUtils.getLength2(checkedMTV3, 0);

    if (lengthChecked < lengthCurrent)
      return checkedMTV3;

    return currentMTV3;
  }
}