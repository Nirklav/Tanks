package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Collisions.ConvexHull;
import com.ThirtyNineEighty.Base.Common.Math.Plane;
import com.ThirtyNineEighty.Base.Common.Stopwatch;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.GameContext;

import com.ThirtyNineEighty.Game.Resources.Sources.FileMapDescriptionSource;
import com.ThirtyNineEighty.Game.TanksContext;
import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Future;

public class Map
  implements IMap
{
  private final static int DirectionsCount = 8;
  private final static int Top = 0;
  private final static int TopLeft = 1;
  private final static int TopRight = 2;
  private final static int Left = 3;
  private final static int Right = 4;
  private final static int Bottom = 5;
  private final static int BottomLeft = 6;
  private final static int BottomRight = 7;

  protected final static float stepSize = 3;
  private final static int maxClosed = 200;
  private final static PathComparator pathLengthComparator = new PathComparator();

  private final HashMap<Long, Projection> projectionsCache;
  private final HashMap<Vector2, Float> pointsCache;

  private final ArrayList<Stopwatch> stopwatches;
  private final Stopwatch findSw;
  private final Stopwatch projectionsAnySw;
  private final Stopwatch minOpenFindSw;
  private final Stopwatch closedCheckSw;

  public final String name;
  public final MapDescription description;

  public Map(String name)
  {
    this.name = name;
    this.description = TanksContext.resources.getMap(new FileMapDescriptionSource(name));
    this.projectionsCache = new HashMap<>();
    this.pointsCache = buildCache(description);

    stopwatches = new ArrayList<>();
    stopwatches.add(findSw = new Stopwatch("findPath"));
    stopwatches.add(projectionsAnySw = new Stopwatch("projectionsAny"));
    stopwatches.add(minOpenFindSw = new Stopwatch("minOpenFind"));
    stopwatches.add(closedCheckSw = new Stopwatch("closedCheck"));
  }

  private static HashMap<Vector2, Float> buildCache(final MapDescription description)
  {
    final Plane plane = new Plane();
    final HashMap<Vector2, Float> pointsCache = new HashMap<>();

    float modulo = description.size % stepSize;
    final float minPoint = -(description.size - modulo);

    IWorld world = GameContext.content.getWorld();
    ArrayList<WorldObject<?, ?>> objects = new ArrayList<>();
    world.getObjects(objects);

    ArrayList<Future<?>> tasks = new ArrayList<>();
    for (final WorldObject<?, ?> object : objects)
    {
      if (object.collidable == null)
        continue;

      Properties properties = object.getProperties();
      if (!properties.isStaticObject())
        continue;

      if (properties.isIgnoreOnMap())
        continue;

      tasks.add(GameContext.threadPool.submit(new Runnable()
      {
        @Override
        public void run()
        {
          Vector2 point = new Vector2();
          ConvexHull hull = new ConvexHull(object.collidable, plane);

          for (float x = minPoint; x < description.size; x += stepSize)
            for (float y = minPoint; y < description.size; y += stepSize)
            {
              point.setFrom(x, y);
              normalizePoint(point);

              if (hull.contains(point))
              {
                synchronized (pointsCache)
                {
                  pointsCache.put(new Vector2(point), 0f);
                }
              }
              else
              {
                float maxRadius = hull.getMaxCircleRadius(point);

                // If radius for point already exist and lass than new then skip it
                synchronized (pointsCache)
                {
                  Float existingRadius = pointsCache.get(point);
                  if (existingRadius != null && existingRadius < maxRadius)
                    continue;

                  pointsCache.put(new Vector2(point), maxRadius);
                }
              }
            }

          Vector2.release(point);
        }
      }));
    }

    try
    {
      for (Future<?> result : tasks)
        result.get();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    return pointsCache;
  }

  public void release()
  {
    TanksContext.resources.release(description);

    for (Projection projection : projectionsCache.values())
      projection.release();

    projectionsCache.clear();
    pointsCache.clear();
  }

  @Override
  public float size()
  {
    return description.size;
  }

  @Override
  public IPath findPath(WorldObject<?, ?> finder, WorldObject<?, ?> target)
  {
    Vector2 start = Vector2.getInstance(finder.getPosition());
    Vector2 end = Vector2.getInstance(target.getPosition());

    Projection finderProjection = getProjection(finder);
    ArrayList<Projection> projections = getProjections(finder, target);

    findSw.start();
    IPath result = findPath(start, end, finderProjection, projections);
    findSw.stop();

    return result;
  }

  private IPath findPath(Vector2 start, Vector2 end, Projection finder, ArrayList<Projection> projections)
  {
    normalizePoint(start);
    normalizePoint(end);

    HashSet<Vector2> closedSet = new HashSet<>();
    HashMap<Vector2, PathNode> openSet = new HashMap<>();
    openSet.put(start, new PathNode(start, end));

    while (openSet.size() > 0)
    {
      // Get next node with minimum path length to end
      minOpenFindSw.start();
      PathNode currentNode = Collections.min(openSet.values(), pathLengthComparator);
      minOpenFindSw.stop();

      // If current node is equals endpoint then we found the path
      if (currentNode.position.equals(end))
        return getPath(finder, currentNode);

      // Set currentNode as processed
      openSet.remove(currentNode.position);
      closedSet.add(currentNode.position);

      // If closed max
      if (closedSet.size() >= maxClosed)
        return null;

      // Process neighbours
      for (int i = 0; i < DirectionsCount; i++)
      {
        Vector2 neighborPoint = getNeighborPoint(currentNode.position, i);

        // If point not exist
        if (neighborPoint == null)
          continue;

        // If closedSet already contains this point then skip it
        closedCheckSw.start();
        try
        {
          if (closedSet.contains(neighborPoint))
            continue;
        }
        finally
        {
          closedCheckSw.stop();
        }

        // If point blocked by static object then skip it
        Float maxRadius = pointsCache.get(neighborPoint);
        if (maxRadius != null && maxRadius < finder.radius())
          continue;

        // If neighborPoint is blocked then skip it
        projectionsAnySw.start();
        try
        {
          if (projections != null && any(projections, new ProjectionPredicate(finder, neighborPoint)))
            continue;
        }
        finally
        {
          projectionsAnySw.stop();
        }

        // Build neighborNode
        PathNode neighborNode = new PathNode(currentNode, neighborPoint, end);

        // If openSet already contain node with this position then select node which is farther from the start
        PathNode existingNode = openSet.get(neighborNode.position);
        if (existingNode == null)
        {
          openSet.put(neighborNode.position, neighborNode);
        }
        else if (existingNode.lengthFromStart > neighborNode.lengthFromStart)
        {
          existingNode.lengthFromStart = neighborNode.lengthFromStart;
          existingNode.from = neighborNode.from;
        }
      }
    }

    return null;
  }

  private ArrayList<Projection> getProjections(WorldObject<?, ?> finder, WorldObject<?, ?> target)
  {
    ArrayList<Projection> result = new ArrayList<>();
    ArrayList<WorldObject<?, ?>> objects = new ArrayList<>();

    IWorld world = GameContext.content.getWorld();
    world.getObjects(objects);

    for (WorldObject<?, ?> object : objects)
    {
      if (target == object || finder == object)
        continue;

      Projection projection = getProjection(object);
      if (projection == null)
        continue;

      result.add(projection);
    }
    return result;
  }

  private Projection getProjection(WorldObject<?, ?> object)
  {
    synchronized (projectionsCache)
    {
      Projection projection = projectionsCache.get(object.getId());
      if (projection != null)
      {
        projection.set();
        return projection;
      }

      projection = Projection.FromObject(object);
      if (projection == null)
        return null;

      projectionsCache.put(object.getId(), projection);
      return projection;
    }
  }

  private static void normalizePoint(Vector point)
  {
    int size = point.getSize();
    for (int i = 0; i < size; i++)
    {
      float value = point.get(i);
      float modulo = value % stepSize;
      value -= modulo;

      if (modulo >= stepSize / 2)
        value += stepSize;

      point.set(i, value);
    }
  }

  private Vector2 getNeighborPoint(Vector2 current, int direction)
  {
    Vector2 result = Vector2.getInstance();
    float x = current.getX();
    float y = current.getY();

    switch (direction)
    {
    case Top:         result.setFrom(x           , y + stepSize); break;
    case TopLeft:     result.setFrom(x - stepSize, y + stepSize); break;
    case TopRight:    result.setFrom(x + stepSize, y + stepSize); break;
    case Left:        result.setFrom(x - stepSize, y           ); break;
    case Right:       result.setFrom(x + stepSize, y           ); break;
    case Bottom:      result.setFrom(x           , y - stepSize); break;
    case BottomLeft:  result.setFrom(x - stepSize, y - stepSize); break;
    case BottomRight: result.setFrom(x + stepSize, y - stepSize); break;
    }

    for (int i = 0; i < result.getSize(); i++)
    {
      float value = result.get(i);
      if (Math.abs(value) > description.size)
      {
        Vector2.release(result);
        return null;
      }
    }

    return result;
  }

  private static IPath getPath(Projection finder, PathNode node)
  {
    ArrayList<Vector2> result = new ArrayList<>();
    PathNode currentNode = node;

    Vector2 prevPosition = null;
    Vector2 lastDirection = Vector2.getInstance();
    Vector2 direction = Vector2.getInstance();

    while (currentNode != null)
    {
      if (prevPosition != null)
      {
        lastDirection.setFrom(direction);

        direction.setFrom(currentNode.position);
        direction.subtract(prevPosition);
        direction.normalize();

        if (direction.equals(lastDirection))
          result.remove(result.size() - 1);
      }

      result.add(currentNode.position);

      prevPosition = currentNode.position;
      currentNode = currentNode.from;
    }

    Vector2.release(lastDirection);
    Vector2.release(direction);

    Collections.reverse(result);
    return new Path(finder.getObject(), result);
  }

  public String getStatistics()
  {
    StringBuilder result = new StringBuilder();
    for (Stopwatch sw : stopwatches)
    {
      result.append(sw.name())
            .append(": ")
            .append(sw.all())
            .append("\n");
    }
    return result.toString();
  }

  private static <T> boolean any(Collection<T> collection, Predicate<T> predicate)
  {
    return find(collection, predicate) != null;
  }

  private static <T> T find(Collection<T> collection, Predicate<T> predicate)
  {
    for (T current : collection)
      if (predicate.apply(current))
        return current;
    return null;
  }

  private static class ProjectionPredicate
    implements Predicate<Projection>
  {
    private final Vector2 point;
    private final Projection finderProjection;

    public ProjectionPredicate(Projection finder, Vector2 point)
    {
      this.point = point;
      this.finderProjection = finder;
    }

    @Override
    public boolean apply(Projection projection)
    {
      WorldObject<?, ?> object = projection.getObject();
      WorldObject<?, ?> finder = finderProjection.getObject();

      //noinspection SimplifiableIfStatement
      if (object.getId().equals(finder.getId()))
        return false;

      float finderRadius = finder.collidable.getRadius();
      return projection.contains(point, finderRadius);
    }
  }

  private static class PathComparator
    implements Comparator<PathNode>
  {
    @Override
    public int compare(PathNode lhs, PathNode rhs)
    {
      float lhsLength = lhs.getFullLength();
      float rhsLength = rhs.getFullLength();
      return Float.compare(lhsLength, rhsLength);
    }
  }
}