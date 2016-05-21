package com.ThirtyNineEighty.Base.Collisions;

import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.GameContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class CollisionManager
{
  private final ArrayList<WorldObject<?, ?>> resolvingObjects = new ArrayList<>();

  public void addToResolving(WorldObject<?, ?> object)
  {
    if (!resolvingObjects.contains(object))
      resolvingObjects.add(object);
  }

  public void resolve(Collection<WorldObject<?, ?>> worldObjects)
  {
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
      Future<Pair> task = GameContext.threadPool.submit(new Callable<Pair>()
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

      IWorld world = GameContext.content.getWorld();

      // Resolving collisions
      for (Future<Pair> task : tasks)
      {
        Pair pair = task.get();

        WorldObject<?, ?> first = pair.first;
        WorldObject<?, ?> second = pair.second;
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

        // first moved
        if (pair.firstMoved && !pair.secondMoved)
        {
          first.move(collision.getMTVLength(), collision.getMTV());
          continue;
        }

        // second moved
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

  private static Collection<Pair> buildPairs(Collection<WorldObject<?, ?>> resolving, Collection<WorldObject<?, ?>> all)
  {
    HashMap<PairKey, Pair> pairs = new HashMap<>();

    for (WorldObject<?, ?> first : resolving)
      for (WorldObject<?, ?> second : all)
      {
        if (first == second)
          continue;

        if (first.collidable == null || second.collidable == null)
          continue;

        if (!first.isInitialized() || !second.isInitialized())
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

  private static float getLength(WorldObject<?, ?> one, WorldObject<?, ?> two)
  {
    Vector3 positionOne = one.collidable.getPosition();
    Vector3 positionTwo = two.collidable.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }
}
