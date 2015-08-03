package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Angle;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

  public void move(GameObject object)
  {
    GameDescription description = object.getDescription();
    object.move(description.getSpeed() * GameContext.getDelta());
    addToResolving(object);
  }

  public void move(EngineObject object, float length)
  {
    object.move(length);
    addToResolving(object);
  }

  public void move(EngineObject object, Vector3 vector, float length)
  {
    object.move(length, vector);
    addToResolving(object);
  }

  public void rotate(GameObject object, float targetAngle)
  {
    GameDescription description = object.getDescription();

    float speed = description.getRotationSpeed() * GameContext.getDelta();
    float objectAngle = object.getAngles().getZ();
    float addedValue = 0;

    if (Math.abs(targetAngle - objectAngle) > speed)
      addedValue = speed * Angle.getDirection(objectAngle, targetAngle);

    Vector3 vector = Vector.getInstance(3);
    vector.addToZ(addedValue);
    object.rotate(vector);
    addToResolving(object);
    Vector.release(vector);
  }

  public void rotate(EngineObject object, Vector3 angles)
  {
    object.rotate(angles);
    addToResolving(object);
  }

  private void addToResolving(EngineObject object)
  {
    if (!resolvingObjects.contains(object))
      resolvingObjects.add(object);

    Map map = GameContext.mapManager.getMap();
    if (map != null)
    {
      Vector3 position = object.getPosition();

      float x = position.getX();
      if (Math.abs(x) >= map.size)
        position.setX(map.size * Math.signum(x));

      float y = position.getY();
      if (Math.abs(y) >= map.size)
        position.setY(map.size * Math.signum(y));
    }
  }

  public void resolve()
  {
    IWorld world = GameContext.content.getWorld();
    if (world == null)
      return;

    // Copy all world objects
    worldObjects.clear();
    world.fillObjects(worldObjects);

    // Normalize all objects locations
    for (EngineObject object : worldObjects)
      if (object.collidable != null)
        object.collidable.normalizeLocation();

    if (resolvingObjects.size() == 0)
      return;

    // Prepare objects
    Collection<Pair> pairs = buildPairs(resolvingObjects, worldObjects);
    if (pairs.size() == 0)
      return;

    // Parallel collision search (should not change world or objects)
    Collection<Future<Pair>> tasks = new ArrayList<>();
    final CountDownLatch latch = new CountDownLatch(pairs.size());
    for (final Pair pair : pairs)
    {
      Future<Pair> task = threadPool.submit(new Callable<Pair>()
      {
        @Override
        public Pair call() throws Exception
        {
          try
          {
            pair.collision = new Collision3D(pair.first.collidable, pair.second.collidable);
            return pair;
          }
          finally
          {
            latch.countDown();
          }
        }
      });
      tasks.add(task);
    }

    try
    {
      // Wait for all tasks will be completed
      latch.await();

      // Resolving collisions
      for (Future<Pair> task : tasks)
      {
        Pair pair = task.get();

        EngineObject first = pair.first;
        EngineObject second = pair.second;
        Description firstDescription = first.getDescription();
        Description secondDescription = second.getDescription();
        Collision3D collision = pair.collision;

        if (!collision.isCollide())
          continue;

        first.collide(second);
        second.collide(first);

        if (firstDescription.removeOnCollide())
          world.remove(first);

        if (secondDescription.removeOnCollide())
          world.remove(second);

        if (firstDescription.removeOnCollide() || secondDescription.removeOnCollide())
          continue;

        if (pair.firstMoved && !pair.secondMoved)
        {
          first.move(collision.getMTVLength(), collision.getMTV());
          continue;
        }

        if (!pair.firstMoved && pair.secondMoved)
        {
          second.move(-collision.getMTVLength(), collision.getMTV());
          continue;
        }

        // first and second moved
        first.move(collision.getMTVLength() / 2, collision.getMTV());
        second.move(-collision.getMTVLength() / 2, collision.getMTV());
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    resolvingObjects.clear();
  }

  private static Collection<Pair> buildPairs(Collection<EngineObject> resolving, Collection<EngineObject> all)
  {
    HashMap<PairKey, Pair> pairs = new HashMap<>();

    for (EngineObject first : resolving)
      for (EngineObject second : all)
      {
        if (first == second)
          continue;

        if (first.collidable == null || second.collidable == null)
          continue;

        if (first.collidable.getRadius() + second.collidable.getRadius() < getLength(first, second))
          continue;

        PairKey key = new PairKey(first, second);
        Pair pair = pairs.get(key);
        if (pair == null)
        {
          pair = new Pair(first, second);
          pairs.put(key, pair);
        }

        // In pair objects can swap
        if (pair.first == first)
          pair.firstMoved = true;
        else
          pair.secondMoved = true;
      }

    return pairs.values();
  }

  private static float getLength(EngineObject one, EngineObject two)
  {
    Vector3 positionOne = one.collidable.getPosition();
    Vector3 positionTwo = two.collidable.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }
}
