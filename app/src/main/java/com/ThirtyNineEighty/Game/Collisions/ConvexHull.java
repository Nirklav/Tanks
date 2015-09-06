package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Common.Math.Plane;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConvexHull
{
  private final ICollidable collidable;
  private final Plane plane;
  private ArrayList<Vector2> convexHull;

  public ConvexHull(ICollidable collidable, Plane plane)
  {
    this.collidable = collidable;
    this.plane = plane;
  }

  public ArrayList<Vector2> get()
  {
    if (convexHull != null)
      return convexHull;

    ArrayList<Vector2> projection = getDistinctProjection(collidable, plane);
    convexHull = new ArrayList<>(projection.size());
    if (projection.size() < 2)
      throw new IllegalStateException("projection size less than 2");

    int firstIndex = getFirstPointIndex(projection);
    Vector2 first = projection.remove(firstIndex);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    Vector2 second = projection.remove(0);
    convexHull.add(second);

    Vector2 prevVector = Vector.getInstance(2);
    Vector2 currentVector = Vector.getInstance(2);

    for(Vector2 current : projection)
    {
      Vector2 firstPrevPoint = convexHull.get(convexHull.size() - 1);
      Vector2 secondPrevPoint = convexHull.get(convexHull.size() - 2);

      prevVector.setFrom(firstPrevPoint);
      prevVector.subtract(secondPrevPoint);

      currentVector.setFrom(current);
      currentVector.subtract(firstPrevPoint);

      float angle = prevVector.getAngle(currentVector);
      if (angle >= 180 && angle < 360)
        convexHull.remove(convexHull.size() - 1);

      convexHull.add(current);
    }

    Vector.release(prevVector);
    Vector.release(currentVector);

    return convexHull;
  }

  private static int getFirstPointIndex(ArrayList<Vector2> projection)
  {
    Vector2 minVector = null;
    int minVectorIndex = 0;

    int size = projection.size();
    for (int i = 0; i < size; i++)
    {
      Vector2 current = projection.get(i);

      if (minVector == null)
      {
        minVector = current;
        continue;
      }

      int compareX = Float.compare(current.getX(), minVector.getX());
      if (compareX < 0)
      {
        minVector = current;
        minVectorIndex = i;
      }

      if (compareX == 0)
      {
        int compareY = Float.compare(current.getY(), minVector.getY());
        if (compareY == 0)
          throw new IllegalArgumentException("projection has the same points");

        if (compareY > 0)
        {
          minVector = current;
          minVectorIndex = i;
        }
      }
    }

    return minVectorIndex;
  }

  private ArrayList<Vector2> getDistinctProjection(ICollidable collidable, Plane plane)
  {
    Vector2 vector = Vector.getInstance(2);
    ArrayList<Vector2> result = new ArrayList<>();

    for (Vector3 current : collidable.getVertices())
    {
      plane.getProjection(vector, current);
      if (!result.contains(vector))
      {
        Vector2 copy = Vector.getInstance(2, vector);
        result.add(copy);
      }
    }

    Vector.release(vector);
    return result;
  }

  private static class AngleComparator implements Comparator<Vector2>
  {
    private Vector2 first;
    private Vector2 left;
    private Vector2 right;

    public AngleComparator(Vector2 first)
    {
      this.first = first;
      left = Vector.getInstance(2);
      right = Vector.getInstance(2);
    }

    @Override
    public int compare(Vector2 lhs, Vector2 rhs)
    {
      // sort angles counterclockwise

      // shift vectors to center
      left.setFrom(lhs);
      left.subtract(first);

      right.setFrom(rhs);
      right.subtract(first);

      // find angles
      float firstAngle = Vector2.xAxis.getAngle(left);
      float secondAngle = Vector2.xAxis.getAngle(right);

      // normalize angles for correct sorting
      // Example: 15, 45, 315, 345 => -45, -15, 15, 45
      if (firstAngle > 90)
        firstAngle -= 360;

      if (secondAngle > 90)
        secondAngle -= 360;

      // if angles equals compare by length
      if (Math.abs(firstAngle - secondAngle) <= Vector.epsilon)
      {
        float leftLength = left.getLength();
        float rightLength = right.getLength();

        // if angle > 0 sort by desc
        if (firstAngle >= 0)
          return Float.compare(rightLength, leftLength);

        return Float.compare(leftLength, rightLength);
      }

      // compare angles
      return Float.compare(firstAngle, secondAngle);
    }
  }

  public void release()
  {
    Vector3.release(convexHull);
  }
}
