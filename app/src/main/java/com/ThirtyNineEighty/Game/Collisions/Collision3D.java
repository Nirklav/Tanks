package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.ArrayList;

public class Collision3D
  extends Collision<Vector3>
{
  private Vector3 mtv;
  private float mtvLength;
  private boolean collide;

  public Collision3D(ICollidable objectOne, ICollidable objectTwo)
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

  private static CheckResult check(ICollidable firstPh, ICollidable secondPh)
  {
    ArrayList<Plane> planes = getPlanes(firstPh, secondPh);

    Collision2D min = null;
    Plane minPlane = new Plane();

    for(Plane plane : planes)
    {
      ArrayList<Vector2> resultOne = firstPh.getConvexHull(plane);
      ArrayList<Vector2> resultTwo = secondPh.getConvexHull(plane);

      Collision2D collision = new Collision2D(resultOne, resultTwo);
      if (!collision.isCollide())
        return null;

      if (min == null || Collision2D.compare(collision, min) < 0)
      {
        min = collision;
        minPlane.setFrom(plane);
      }
    }

    return new CheckResult(min, minPlane);
  }

  private static ArrayList<Plane> getPlanes(ICollidable firstPh, ICollidable secondPh)
  {
    ArrayList<Plane> planes = new ArrayList<Plane>();
    ArrayList<Vector3> firstNormals = firstPh.getGlobalNormals();
    ArrayList<Vector3> secondNormals = secondPh.getGlobalNormals();

    int size = firstNormals.size() + secondNormals.size();

    Plane plane = new Plane();
    Plane xPlane = new Plane();
    Plane yPlane = new Plane();

    for(int i = 0; i < size; i++)
    {
      setPlane(plane, firstNormals, secondNormals, i);

      xPlane.setFrom(plane.xAxis());
      yPlane.setFrom(plane.yAxis());

      if (!planes.contains(xPlane))
        planes.add(new Plane(xPlane));

      if (!planes.contains(yPlane))
        planes.add(new Plane(yPlane));
    }

    return planes;
  }

  private static void setPlane(Plane plane, ArrayList<Vector3> firstNormals, ArrayList<Vector3> secondNormals, int num)
  {
    if (num < firstNormals.size())
      plane.setFrom(firstNormals.get(num));
    else
    {
      num -= secondNormals.size();
      plane.setFrom(secondNormals.get(num));
    }
  }

  private static Vector3 getMTV(CheckResult result)
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

  private static class CheckResult
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
