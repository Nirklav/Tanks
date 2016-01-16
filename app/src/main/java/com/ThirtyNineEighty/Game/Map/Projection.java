package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.Collision2D;
import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class Projection
{
  private static final Plane plane = new Plane();

  private final WorldObject<?, ?> object;
  private final ConvexHull hull;
  private Vector2 position;

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
    this.position = Vector.getInstance(2, object.collidable.getPosition());
  }

  public void setPosition(Vector2 value)
  {
    Vector2 delta = value.getSubtract(position);
    position.setFrom(value);

    for (Vector2 hullPoint : hull.get())
      hullPoint.add(delta);

    Vector.release(delta);
  }

  public WorldObject<?, ?> getObject()
  {
    return object;
  }

  public boolean isIntersect(Projection projection)
  {
    Collision2D collision = new Collision2D(hull.get(), projection.hull.get());
    return collision.isCollide();
  }

  public boolean contains(Vector2 point)
  {
    return hull.contains(point);
  }
}
