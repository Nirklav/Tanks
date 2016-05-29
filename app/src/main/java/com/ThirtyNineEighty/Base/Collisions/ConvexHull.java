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
  private final IHullProvider provider;

  private ArrayList<Vector2> convexHull;
  private Vector2 center;
  private float radius;

  public ConvexHull(Collidable collidable, Plane plane)
  {
    provider = new CollidableHullProvider(collidable, plane);
    radius = -1;
  }

  public ConvexHull(ArrayList<Vector2> hull)
  {
    provider = new RawHullProvider(hull);
    radius = -1;
  }

  public boolean contains(Vector2 point)
  {
    if (!canContainsPoint(point))
      return false;

    if (convexHull == null)
      convexHull = provider.get();

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

  private boolean canContainsPoint(Vector2 point)
  {
    Vector2 center = getCenter();
    float radius = getRadius();

    Vector2 vec = point.getSubtract(center);
    float length = vec.getLength();
    Vector2.release(vec);

    return length < radius;
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

  public boolean isIntersectWithCircle(Vector2 circleCenter, float circleRadius)
  {
    // Optimization
    if (!canIntersectWithCircle(circleCenter, circleRadius))
      return false;

    float maxRadius = getMaxCircleRadius(circleCenter);
    return circleRadius > maxRadius;
  }

  private boolean canIntersectWithCircle(Vector2 otherCenter, float otherRadius)
  {
    Vector2 center = getCenter();
    float radius = getRadius();

    Vector2 vec = otherCenter.getSubtract(center);
    float length = vec.getLength();
    Vector2.release(vec);

    return length < radius + otherRadius;
  }

  public float getMaxCircleRadius(Vector2 circleCenter)
  {
    // Get hull
    if (convexHull == null)
      convexHull = provider.get();

    float circleRadius = Float.MAX_VALUE;

    // Check convex hull vertices
    Vector2 vector = Vector2.getInstance();
    try
    {
      for (Vector2 point : convexHull)
      {
        vector.setFrom(circleCenter);
        vector.subtract(point);

        float length = vector.getLength();
        if (length < circleRadius)
          circleRadius = length;
      }
    }
    finally
    {
      Vector2.release(vector);
    }

    // Check convex hull edges
    int count = convexHull.size();
    Vector2 lineNormal = Vector2.getInstance();
    Vector2 intersectPoint = Vector2.getInstance();
    Vector2 intersectToCenter = Vector2.getInstance();
    try
    {
      for (int i = 0; i < count; i++)
      {
        Vector2 firstPoint = convexHull.get(i);
        Vector2 secondPoint = convexHull.get(i + 1 == count ? 0 : i + 1);

        intersectPoint.setFrom(circleCenter);
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
        intersectToCenter.subtract(circleCenter);

        float length = intersectToCenter.getLength();
        if (length < circleRadius)
          circleRadius = length;
      }
    }
    finally
    {
      Vector2.release(lineNormal);
      Vector2.release(intersectPoint);
      Vector2.release(intersectToCenter);
    }

    return circleRadius;
  }

  public Vector2 getCenter()
  {
    if (center != null)
      return center;

    if (convexHull == null)
      convexHull = provider.get();

    int size = convexHull.size();

    float area2 = 0;
    float sumX = 0;
    float sumY = 0;
    for (int i = 0; i < size; i++)
    {
      int nextI = i + 1 == size ? 0 : i + 1;

      Vector2 c = convexHull.get(i);
      Vector2 n = convexHull.get(nextI);

      float ex = c.getX() + n.getX();
      float ey = c.getY() + n.getY();
      float e = c.getX() * n.getY() - n.getX() * c.getY();

      sumX += ex * e;
      sumY += ey * e;
      area2 += e;
    }

    float area = area2 / 2;
    center = Vector2.getInstance(sumX / (6 * area), sumY / (6 * area));
    return center;
  }

  public float getRadius()
  {
    if (radius > 0)
      return radius;

    if (convexHull == null)
      convexHull = provider.get();

    Vector2 fromCenter = Vector2.getInstance();
    Vector2 center = getCenter();

    for (Vector2 vec : convexHull)
    {
      fromCenter.setFrom(vec);
      fromCenter.subtract(center);
      float length = fromCenter.getLength();

      if (radius < length)
        radius = length;
    }

    Vector2.release(fromCenter);
    return radius;
  }

  public void move(Vector2 delta)
  {
    Vector2 center = getCenter();

    center.add(delta);
    for (Vector2 vec : convexHull)
      vec.add(delta);
  }

  public void rotate(float angle)
  {
    Vector2 center = getCenter();

    for (Vector2 vec : convexHull)
    {
      vec.subtract(center);
      vec.rotate(angle);
      vec.add(center);
    }
  }

  public ArrayList<Vector2> get()
  {
    if (convexHull == null)
      convexHull = provider.get();
    return convexHull;
  }

  public void release()
  {
    if (convexHull != null)
      Vector2.release(convexHull);
  }

  private interface IHullProvider
  {
    ArrayList<Vector2> get();
  }

  private static class RawHullProvider
    implements IHullProvider
  {
    private final ArrayList<Vector2> hull;

    public RawHullProvider(ArrayList<Vector2> hull)
    {
      this.hull = hull;
    }

    @Override
    public ArrayList<Vector2> get()
    {
      return hull;
    }
  }

  private static class CollidableHullProvider
    implements IHullProvider
  {
    private final Collidable collidable;
    private final Plane plane;

    public CollidableHullProvider(Collidable collidable, Plane plane)
    {
      this.collidable = collidable;
      this.plane = plane;
    }

    public ArrayList<Vector2> get()
    {
      return build(collidable, plane);
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
  }
}
