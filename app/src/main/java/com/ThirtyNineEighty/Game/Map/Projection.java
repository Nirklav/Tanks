package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class Projection
{
  private static final Plane plane = new Plane();

  private final Object syncObject;
  private final WorldObject<?, ?> object;

  private ConvexHull hull;
  private Vector3 position;
  private Vector3 angles;

  private Vector2 projPosition;
  private float projAngle;

  public static Projection FromObject(WorldObject<?, ?> object)
  {
    if (object.collidable == null)
      return null;

    Properties properties = object.getProperties();
    if (properties.isIgnoreOnMap())
      return null;

    if (properties.isStaticObject())
      return null;

    return new Projection(object);
  }

  private Projection(WorldObject<?, ?> object)
  {
    this.object = object;
    this.position = Vector3.getInstance();
    this.angles = Vector3.getInstance();
    this.projPosition = Vector2.getInstance();
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
        && currentPosition.equals(position)
        && currentAngles.equals(angles))
        return;

      Vector2 currentProjPosition = plane.getProjection(currentPosition);
      float currentProjAngle = currentAngles.getZ();

      if (hull == null)
        hull = new ConvexHull(object.collidable, plane);
      else
      {
        Vector2 deltaPos = currentProjPosition.getSubtract(projPosition);
        float deltaZ = currentProjAngle - projAngle;

        hull.move(deltaPos);
        hull.rotate(deltaZ);

        Vector2.release(deltaPos);
      }

      position.setFrom(currentPosition);
      angles.setFrom(angles);

      projPosition.setFrom(currentProjPosition);
      projAngle = currentProjAngle;
    }
  }

  public WorldObject<?, ?> getObject()
  {
    return object;
  }

  public boolean contains(Vector2 otherCenter, float otherRadius)
  {
    synchronized (syncObject)
    {
      return hull.isIntersectWithCircle(otherCenter, otherRadius);
    }
  }

  public float radius()
  {
    synchronized (syncObject)
    {
      return hull.getRadius();
    }
  }

  public void release()
  {
    synchronized (syncObject)
    {
      hull.release();

      Vector3.release(position);
      Vector3.release(angles);
    }
  }
}
