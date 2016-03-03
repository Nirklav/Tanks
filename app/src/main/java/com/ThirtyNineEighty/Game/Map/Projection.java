package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class Projection
{
  private static final Plane plane = new Plane();

  private final WorldObject<?, ?> object;
  private Vector3 hullPosition;
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
    this.hullPosition = Vector3.getInstance();

    set();
  }

  public void set()
  {
    Vector3 currentPosition = object.collidable.getPosition();
    if (hull != null && currentPosition.equals(hullPosition))
      return;

    if (hull != null)
      hull.release();

    hullPosition.setFrom(currentPosition);
    hull = new ConvexHull(object.collidable, plane);
  }

  public WorldObject<?, ?> getObject()
  {
    return object;
  }

  public boolean contains(Vector2 point, float radius)
  {
    return hull.isIntersectWithCircle(point, radius);
  }

  public void release()
  {
    if (hull != null)
      hull.release();

    Vector3.release(hullPosition);
  }
}
