package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConvexHull
{
  private final Collidable collidable;
  private final Plane plane;
  private ArrayList<Vector2> convexHull;

  public ConvexHull(Collidable collidable, Plane plane)
  {
    this.collidable = collidable;
    this.plane = plane;
  }

  public boolean contains(Vector2 point)
  {
    if (convexHull == null)
      convexHull = build(collidable, plane);

    int count = convexHull.size();
    Vector2 normal = Vector2.getInstance();

    for (int i = 0; i < count; i ++)
    {
      setNormal(normal, convexHull, i);

      Vector2 projection = normal.getProjection(convexHull);
      float pointProjection = point.getScalar(normal);

      if (projection.getX() < pointProjection || projection.getY() > pointProjection)
      {
        Vector2.release(normal);
        return false;
      }
    }

    Vector2.release(normal);
    return true;
  }

  // TODO: create figures types and move this method out of here
  public boolean isIntersectWithCircle(Vector2 center, float pointRadius)
  {
    // optimization
    if (!canIntersectWithCircle(center, pointRadius))
      return false;

    // build convex hull
    if (convexHull == null)
      convexHull = build(collidable, plane);

    // Check convex hull vertices
    Vector2 vector = Vector2.getInstance();

    for (Vector2 point : convexHull)
    {
      vector.setFrom(center);
      vector.subtract(point);

      if (vector.getLength() < pointRadius)
      {
        Vector2.release(vector);
        return true;
      }
    }
    Vector2.release(vector);

    // Check convex hull edges
    int count = convexHull.size();
    Vector2 lineNormal = Vector2.getInstance();
    Vector2 intersectPoint = Vector2.getInstance();
    Vector2 intersectToCenter = Vector2.getInstance();

    for (int i = 0; i < count; i++)
    {
      Vector2 firstPoint = convexHull.get(i);
      Vector2 secondPoint = convexHull.get(i + 1 == count ? 0 : i + 1);

      intersectPoint.setFrom(center);
      intersectPoint.lineProjection(firstPoint, secondPoint);

      lineNormal.setFrom(secondPoint);
      lineNormal.subtract(firstPoint);
      lineNormal.normalize();

      float firstProjection = lineNormal.getScalar(firstPoint);
      float secondProjection = lineNormal.getScalar(secondPoint);
      float intersectProjection = lineNormal.getScalar(intersectPoint);

      if (intersectProjection < firstProjection || intersectProjection > secondProjection)
        continue;

      intersectToCenter.setFrom(intersectPoint);
      intersectToCenter.subtract(center);

      if (intersectToCenter.getLength() > pointRadius)
        continue;

      Vector2.release(lineNormal);
      Vector2.release(intersectPoint);
      Vector2.release(intersectToCenter);
      return true;
    }

    Vector2.release(lineNormal);
    Vector2.release(intersectPoint);
    Vector2.release(intersectToCenter);
    return false;
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

  private boolean canIntersectWithCircle(Vector2 center, float pointRadius)
  {
    float collidableRadius = collidable.getRadius();
    Vector2 collidablePosition = plane.getProjection(collidable.getPosition());
    Vector2 delta = center.getSubtract(collidablePosition);
    float length = delta.getLength();

    Vector2.release(collidablePosition);
    Vector2.release(delta);

    return length < pointRadius + collidableRadius;
  }

  public ArrayList<Vector2> get()
  {
    if (convexHull == null)
      convexHull = build(collidable, plane);
    return convexHull;
  }

  private static ArrayList<Vector2> build(Collidable collidable, Plane plane)
  {
    ArrayList<Vector2> projection = getDistinctProjection(collidable, plane);
    ArrayList<Vector2> convexHull = new ArrayList<>(projection.size());
    if (projection.size() < 2)
      throw new IllegalStateException("projection size less than 2");

    int firstIndex = getFirstPointIndex(projection);
    Vector2 first = projection.remove(firstIndex);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    Vector2 second = projection.remove(0);
    convexHull.add(second);

    Vector2 prevVector = Vector2.getInstance();
    Vector2 currentVector = Vector2.getInstance();

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

    Vector2.release(prevVector);
    Vector2.release(currentVector);

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

  private static ArrayList<Vector2> getDistinctProjection(Collidable collidable, Plane plane)
  {
    Vector2 vector = Vector2.getInstance();
    ArrayList<Vector2> result = new ArrayList<>();

    for (Vector3 current : collidable.getVertices())
    {
      plane.getProjection(vector, current);
      if (!result.contains(vector))
      {
        Vector2 copy = Vector2.getInstance(vector);
        result.add(copy);
      }
    }

    Vector2.release(vector);
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
      left = Vector2.getInstance();
      right = Vector2.getInstance();
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
    Vector2.release(convexHull);
  }
}
