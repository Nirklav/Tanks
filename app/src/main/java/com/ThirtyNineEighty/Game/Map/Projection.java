package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class Projection
{
  private static final Plane plane = new Plane();

  private final Object syncObject;
  private final WorldObject<?, ?> object;
  private Vector3 collidablePosition;
  private Vector3 collidableAngles;
  private ConvexHull hull;

  public static Projection FromObject(WorldObject<?, ?> object)
  {
    if (object.collidable == null)
      return null;

    return new Projection(object);
  }

  private Projection(WorldObject<?, ?> object)
  {
    this.object = object;
    this.collidablePosition = Vector3.getInstance();
    this.collidableAngles = Vector3.getInstance();
    this.syncObject = new Object();

    set();
  }

  public void set()
  {
    synchronized (syncObject)
    {
      Vector3 currentPosition = object.collidable.getPosition();
      Vector3 currentAngles = object.collidable.getAngles();

      if (hull != null
        && currentPosition.equals(collidablePosition)
        && currentAngles.equals(collidableAngles))
        return;

      if (hull != null)
        hull.release();

      collidablePosition.setFrom(currentPosition);
      collidableAngles.setFrom(currentAngles);

      hull = new ConvexHull(object.collidable, plane);
    }
  }

  public WorldObject<?, ?> getObject()
  {
    return object;
  }

  public boolean contains(Vector2 point)
  {
    synchronized (syncObject)
    {
      return hull.contains(point);
    }
  }

  public boolean contains(Vector2 point, float radius)
  {
    synchronized (syncObject)
    {
      return hull.isIntersectWithCircle(point, radius);
    }
  }

  public void release()
  {
    synchronized (syncObject)
    {
      if (hull != null)
        hull.release();

      Vector3.release(collidablePosition);
      Vector3.release(collidableAngles);
    }
  }
}
