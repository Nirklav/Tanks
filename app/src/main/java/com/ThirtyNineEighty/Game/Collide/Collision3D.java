package com.ThirtyNineEighty.Game.Collide;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;
import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.Vector;

public class Collision3D
  extends Collision<Vector3>
{
  private Vector3 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision3D(IPhysicalObject objectOne, IPhysicalObject objectTwo)
  {
    CheckResult result = check(objectOne, objectTwo);

    if (result == null)
    {
      collide = false;
      return;
    }

    collide = true;
    mtv = getMTV(result);
    mtvLength = result.collision.getMTVLength();
  }

  private CheckResult check(IPhysicalObject firstPh, IPhysicalObject secondPh)
  {
    Vector<Vector3> firstNormals = firstPh.getGlobalNormals();
    Vector<Vector3> secondNormals = secondPh.getGlobalNormals();

    Collision2D min = null;
    Plane minPlane = null;
    Plane plane = new Plane();

    int count = firstNormals.size() + secondNormals.size();

    for (int i = 0; i < count; i++)
    {
      setPlane(plane, firstNormals, secondNormals, i);

      Vector<Vector2> resultOne = firstPh.getConvexHull(plane);
      Vector<Vector2> resultTwo = secondPh.getConvexHull(plane);

      Collision2D collision = new Collision2D(resultOne, resultTwo);
      if (!collision.isCollide())
        return null;

      if (min == null || Collision2D.compare(collision, min) < 0)
      {
        min = collision;
        minPlane = plane;
      }
    }

    return new CheckResult(min, minPlane);
  }

  public void setPlane(Plane plane, Vector<Vector3> firstNormals, Vector<Vector3> secondNormals, int num)
  {
    if (num < firstNormals.size())
      plane.setFrom(firstNormals.get(num));
    else
    {
      num -= secondNormals.size();
      plane.setFrom(secondNormals.get(num));
    }
  }

  private Vector3 getMTV(CheckResult result)
  {
    Vector2 mtv2 = result.collision.getMTV();
    Vector3 mtv3 = new Vector3(mtv2.getX(), mtv2.getY(), 0);

    Vector3 planeX = result.plane.xAxis();
    Vector3 planeY = result.plane.yAxis();
    Vector3 planeZ = result.plane.zAxis();

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

  @Override
  public Vector3 getMTV()
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
    public Collision2D collision;
    public Plane plane;

    public CheckResult(Collision2D collision, Plane plane)
    {
      this.collision = collision;
      this.plane = plane;
    }
  }
}
