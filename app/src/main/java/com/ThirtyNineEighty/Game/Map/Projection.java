package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;

import java.util.ArrayList;

class Projection
{
  private static final Plane plane = new Plane();

  private final float radius;
  private Vector2 position;

  public static Projection FromObject(EngineObject object)
  {
    if (object.collidable == null)
      return null;

    Vector2 position = Vector.getInstance(2, object.getPosition());
    ArrayList<Vector2> vertices = object.collidable.getConvexHull(plane);

    float radius = 0.0f;
    Vector2 tempVector = Vector.getInstance(2);

    for (Vector2 vec : vertices)
    {
      tempVector.setFrom(vec);
      tempVector.subtract(position);

      float length = tempVector.getLength();
      if (length > radius)
        radius = length;
    }

    Vector.release(vertices);
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
