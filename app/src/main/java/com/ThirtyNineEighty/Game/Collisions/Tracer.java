package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public class Tracer
{
  private static final float extension = 1.0f;

  private final Vector3 start;
  private final Vector3 end;
  private final EngineObject[] ignoring;

  private boolean calculated;
  private boolean result;

  public Tracer(EngineObject watcher, EngineObject target)
  {
    this(getPosition(watcher), getPosition(target), watcher, target);
  }

  public Tracer(Vector3 start, Vector3 end, EngineObject... ignoring)
  {
    this.start = start;
    this.end = end;
    this.ignoring = ignoring;
  }

  private static Vector3 getPosition(EngineObject object)
  {
    ICollidable targetCollidable = object.getCollidable();
    if (targetCollidable != null)
      return targetCollidable.getPosition();
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
    ArrayList<EngineObject> worldObjects = new ArrayList<>();

    IWorld world = GameContext.content.getWorld();
    world.fillObjects(worldObjects);

    Vector3 vector = end.getSubtract(start);
    Plane plane = new Plane(vector);
    Vector2 lineProjection = plane.getProjection(start);
    float fullLength = vector.getLength();

    for (EngineObject object : worldObjects)
    {
      if (contains(ignoring, object))
        continue;

      ICollidable collidable = object.getCollidable();
      if (collidable == null)
        continue;

      Vector3 position = object.getPosition();
      Vector3 projection = position.getLineProjection(start, end);

      vector.setFrom(projection);
      vector.subtract(start);
      float firstLength = vector.getLength();

      vector.setFrom(end);
      vector.subtract(projection);
      float secondLength = vector.getLength();

      Vector.release(projection);

      if (secondLength + firstLength > fullLength + extension)
        continue;

      Vector2 lengthVector = plane.getProjection(position);
      lengthVector.subtract(lineProjection);
      if (lengthVector.getLength() > collidable.getRadius())
        continue;

      ArrayList<Vector2> convexHull = collidable.getConvexHull(plane);
      if (contain(convexHull, lineProjection))
        return true;
    }

    Vector.release(vector);
    return false;
  }

  private boolean contains(EngineObject[] objects, EngineObject object)
  {
    for (EngineObject current : objects)
      if (current == object)
        return true;
    return false;
  }

  private boolean contain(ArrayList<Vector2> convexHull, Vector2 point)
  {
    Vector2 normal = Vector.getInstance(2);
    int count = convexHull.size();

    for (int i = 0; i < count; i ++)
    {
      setNormal(normal, convexHull, i);

      Vector2 projection = normal.getProjection(convexHull);
      float pointProjection = point.getScalar(normal);

      if (projection.getX() < pointProjection || projection.getY() > pointProjection)
        return false;
    }

    return true;
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
}
