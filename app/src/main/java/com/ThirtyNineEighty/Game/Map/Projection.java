package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class Projection
{
  private static final Plane plane = new Plane();

  private final WorldObject<?, ?> object;
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
    this.hull = new ConvexHull(object.collidable, plane);
  }

  public void set()
  {
    hull.release();
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
}
