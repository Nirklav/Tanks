package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Collisions.ConvexHull;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Common.Math.Plane;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;

import java.util.ArrayList;

class Projection
{
  private static final Plane plane = new Plane();

  private final float radius;
  private Vector2 position;

  public static Projection FromObject(WorldObject object)
  {
    if (object.collidable == null)
      return null;

    ConvexHull hull = new ConvexHull(object.collidable, plane);
    Vector2 position = Vector.getInstance(2, object.getPosition());

    float radius = 0.0f;
    Vector2 tempVector = Vector.getInstance(2);

    for (Vector2 vec : hull.get())
    {
      tempVector.setFrom(vec);
      tempVector.subtract(position);

      float length = tempVector.getLength();
      if (length > radius)
        radius = length;
    }

    hull.release();
    Vector.release(tempVector);
    return new Projection(radius);
  }

  private Projection(float radius)
  {
    this.radius = radius;
    this.position = Vector.getInstance(2);
  }

  public void setPosition(Vector2 value)
  {
    position.setFrom(value);
  }

  public boolean contains(Vector2 vector, float finderRadius)
  {
    Vector2 tempVector = Vector.getInstance(2, vector);
    tempVector.subtract(position);
    return radius + finderRadius > tempVector.getLength();
  }

  public float getRadius() { return radius; }
}
