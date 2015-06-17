package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.Game.Gameplay.Map;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Angle;
import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollisionManager
{
  private final ArrayList<EngineObject> resolvingObjects;
  private final ArrayList<EngineObject> worldObjects;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();

  public CollisionManager()
  {
    resolvingObjects = new ArrayList<>();
    worldObjects = new ArrayList<>();
  }

  //TODO: rename (something like "trace" or ...)
  public boolean isVisible(EngineObject watcher, EngineObject target)
  {
    IWorld world = GameContext.content.getWorld();
    world.fillObjects(worldObjects);

    Vector3 start = watcher.getPosition();
    Vector3 end = target.getPosition();

    Vector3 vector = end.getSubtract(start);
    Plane plane = new Plane(vector);
    Vector2 lineProjection = plane.getProjection(start);
    float fullLength = vector.getLength();

    for (EngineObject object : worldObjects)
    {
      if (object == watcher || object == target)
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

      if (secondLength + firstLength > fullLength)
        continue;

      Vector2 lengthVector = plane.getProjection(position);
      lengthVector.subtract(lineProjection);
      if (lengthVector.getLength() > collidable.getRadius())
        continue;

      ArrayList<Vector2> convexHull = collidable.getConvexHull(plane);
      if (contain(convexHull, lineProjection))
        return false;
    }

    Vector.release(vector);
    return true;
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

  public void move(GameObject object)
  {
    Characteristic c = object.getCharacteristics();
    object.onMoved(c.getSpeed() * GameContext.getDelta());
    addToResolving(object);
  }

  public void move(EngineObject object, float length)
  {
    object.onMoved(length);
    addToResolving(object);
  }

  public void move(EngineObject object, Vector3 vector, float length)
  {
    object.onMoved(length, vector);
    addToResolving(object);
  }

  public void rotate(GameObject object, float targetAngle)
  {
    Characteristic c = object.getCharacteristics();

    float speed = c.getRotationSpeed() * GameContext.getDelta();
    float objectAngle = object.getAngles().getZ();
    float addedValue = 0;

    if (Math.abs(targetAngle - objectAngle) > speed)
      addedValue = speed * Angle.getDirection(objectAngle, targetAngle);

    Vector3 vector = Vector.getInstance(3);
    vector.addToZ(addedValue);
    object.onRotates(vector);
    addToResolving(object);
    Vector.release(vector);
  }

  public void rotate(EngineObject object, Vector3 angles)
  {
    object.onRotates(angles);
    addToResolving(object);
  }

  public void resolve()
  {
    IWorld world = GameContext.content.getWorld();

    // Copy all world objects
    worldObjects.clear();
    world.fillObjects(worldObjects);

    // Set current global positions for all objects
    for (EngineObject current : worldObjects)
      current.setGlobalCollidablePosition();

    // Parallel collision search (should not change world or objects)
    ArrayList<Future<ResolveResult>> results = new ArrayList<>(resolvingObjects.size());

    final CountDownLatch latch = new CountDownLatch(resolvingObjects.size());
    for (final EngineObject current : resolvingObjects)
    {
      Future<ResolveResult> futureResult = threadPool.submit(
        new Callable<ResolveResult>()
        {
          @Override
          public ResolveResult call() throws Exception
          {
            try
            {
              return resolve(current);
            }
            finally
            {
              latch.countDown();
            }
          }
        }
      );

      results.add(futureResult);
    }

    try
    {
      // Wait for all tasks will be completed
      latch.await();

      // Resolving collisions
      int size = results.size();
      for (int i = size - 1; i >= 0; i--)
      {
        Future<ResolveResult> current = results.get(i);

        ResolveResult result = current.get();
        if (result == null)
          continue;

        EngineObject object = result.checkedObject;
        for (CollisionResult collResult : result.collisions)
        {
          Collision3D collision = collResult.collision;

          object.onMoved(collision.getMTVLength(), collision.getMTV());
          object.onCollide(collResult.collidedObject);
        }
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    resolvingObjects.clear();
  }

  private void addToResolving(EngineObject object)
  {
    if (!resolvingObjects.contains(object))
      resolvingObjects.add(object);

    Map map = GameContext.mapManager.getMap();
    Vector3 position = object.getPosition();

    float x = position.getX();
    if (Math.abs(x) >= map.size)
      position.setX(map.size * Math.signum(x));

    float y = position.getY();
    if (Math.abs(y) >= map.size)
      position.setY(map.size * Math.signum(y));
  }

  private ResolveResult resolve(EngineObject object)
  {
    ICollidable objectPh = object.getCollidable();
    if (objectPh == null)
      return null;

    ResolveResult result = null;

    for (EngineObject current : worldObjects)
    {
      if (object == current)
        continue;

      ICollidable currentPh = current.getCollidable();
      if (currentPh == null)
        continue;

      if (objectPh.getRadius() + currentPh.getRadius() < getLength(object, current))
        continue;

      Collision3D collision = new Collision3D(objectPh, currentPh);
      if (collision.isCollide())
      {
        if (result == null)
          result = new ResolveResult(object);

        result.collisions.add(new CollisionResult(current, collision));
      }
    }

    return result;
  }

  private float getLength(EngineObject one, EngineObject two)
  {
    Vector3 positionOne = one.getPosition();
    Vector3 positionTwo = two.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }

  private static class ResolveResult
  {
    public final EngineObject checkedObject;
    public final LinkedList<CollisionResult> collisions;

    public ResolveResult(EngineObject obj)
    {
      checkedObject = obj;
      collisions = new LinkedList<>();
    }
  }

  private static class CollisionResult
  {
    public final EngineObject collidedObject;
    public final Collision3D collision;

    public CollisionResult(EngineObject obj, Collision3D coll)
    {
      collidedObject = obj;
      collision = coll;
    }
  }
}
