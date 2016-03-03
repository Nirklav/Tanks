package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.GameContext;

import java.util.ArrayList;

public class Tracer
{
  private static final float extension = 0.5f;

  private final Vector3 start;
  private final Vector3 end;
  private final WorldObject<?, ?>[] ignoring;

  private boolean calculated;
  private boolean result;

  public Tracer(WorldObject<?, ?> watcher, WorldObject<?, ?> target)
  {
    this(getPosition(watcher), getPosition(target), watcher, target);
  }

  public Tracer(Vector3 start, Vector3 end, WorldObject<?, ?>... ignoring)
  {
    this.start = start;
    this.end = end;
    this.ignoring = ignoring;
  }

  private static Vector3 getPosition(WorldObject<?, ?> object)
  {
    if (object.collidable != null)
      return object.collidable.getPosition();
    return object.getPosition();
  }

  public boolean intersect()
  {
    if (calculated)
      return result;

    calculated = true;
    return result = calculate();
  }

  private boolean calculate()
  {
    ArrayList<WorldObject<?, ?>> worldObjects = new ArrayList<>();

    IWorld world = GameContext.content.getWorld();
    world.getObjects(worldObjects);

    Vector3 vector = end.getSubtract(start);
    Plane plane = new Plane(vector);
    Vector2 lineProjection = plane.getProjection(start);
    float fullLength = vector.getLength();

    for (WorldObject<?, ?> object : worldObjects)
    {
      if (object.collidable == null)
        continue;

      if (contains(ignoring, object))
        continue;

      Vector3 position = object.collidable.getPosition();
      Vector3 projection = position.getLineProjection(start, end);

      vector.setFrom(projection);
      vector.subtract(start);
      float firstLength = vector.getLength();

      vector.setFrom(end);
      vector.subtract(projection);
      float secondLength = vector.getLength();

      Vector3.release(projection);

      if (secondLength + firstLength > fullLength + extension)
        continue;

      Vector2 lengthVector = plane.getProjection(position);
      lengthVector.subtract(lineProjection);
      if (lengthVector.getLength() > object.collidable.getRadius())
        continue;

      ConvexHull hull = new ConvexHull(object.collidable, plane);
      boolean result = hull.contains(lineProjection);
      hull.release();

      if (result)
        return true;
    }

    Vector3.release(vector);
    return false;
  }

  private boolean contains(WorldObject<?, ?>[] objects, WorldObject<?, ?> object)
  {
    for (WorldObject<?, ?> current : objects)
      if (current == object)
        return true;
    return false;
  }
}
