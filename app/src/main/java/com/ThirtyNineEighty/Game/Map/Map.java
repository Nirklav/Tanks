package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Map.IPath;
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

  private final static float stepSize = 3;
  private final static int maxClosed = 50;
  private final static PathComparator pathLengthComparator = new PathComparator();

  private final HashMap<Long, Projection> projectionsCache;

  public final String name;
  public final MapDescription description;

  public Map(String name)
  {
    this.name = name;
    this.description = TanksContext.resources.getMap(new FileMapDescriptionSource(name));
    this.projectionsCache = new HashMap<>();
  }

  public void release()
  {
    TanksContext.resources.release(description);

    for (Projection projection : projectionsCache.values())
      projection.release();
  }

  @Override
  public float size()
  {
    return description.size;
  }

  @Override
  public IPath findPath(WorldObject<?, ?> finder, WorldObject<?, ?> target)
  {
    Projection finderProjection = getProjection(finder);

    Vector2 start = Vector2.getInstance(finder.getPosition());
    Vector2 end = Vector2.getInstance(target.getPosition());

    return findPath(start, end, finderProjection, getProjections(finder, target));
  }

  private IPath findPath(Vector2 start, Vector2 end, Projection finder, ArrayList<Projection> projections)
  {
    normalizePoint(start);
    normalizePoint(end);

    ArrayList<PathNode> closedSet = new ArrayList<>();
    ArrayList<PathNode> openSet = new ArrayList<>();
    openSet.add(new PathNode(start, 0, getPathLength(start, end)));

    while (openSet.size() > 0)
    {
      // Get next node with minimum path length to end
      PathNode currentNode = Collections.min(openSet, pathLengthComparator);

      // If current node is equals endpoint then we found the path
      if (currentNode.position.equals(end))
        return getPath(finder, currentNode);

      // Set currentNode as processed
      openSet.remove(currentNode);
      closedSet.add(currentNode);

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
        if (any(closedSet, new PositionEqualsPredicate(neighborPoint)))
          continue;

        // If neighborPoint is blocked then skip it
        if (projections != null && any(projections, new ProjectionPredicate(finder, neighborPoint, end)))
          continue;

        // Build neighborNode
        float estimatedLength = getPathLength(neighborPoint, end);
        float lengthFromStart = currentNode.lengthFromStart + stepSize;
        PathNode neighborNode = new PathNode(currentNode, neighborPoint, lengthFromStart, estimatedLength);

        // If openSet already contain node with this position then select node which is farther from the start
        PathNode openNode = find(openSet, new PositionEqualsPredicate(neighborNode.position));
        if (openNode == null)
          openSet.add(neighborNode);
        else
          if (openNode.lengthFromStart > neighborNode.lengthFromStart)
          {
            openNode.from = currentNode;
            openNode.lengthFromStart = neighborNode.lengthFromStart;
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
    projection.set();
    return projection;
  }

  private void normalizePoint(Vector point)
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

  @SuppressWarnings("SuspiciousNameCombination")
  private static float getPathLength(Vector2 start, Vector2 end)
  {
    float x = Math.abs(start.getX() - end.getX());
    float y = Math.abs(start.getY() - end.getY());
    return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
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

  private static class PositionEqualsPredicate
    implements Predicate<PathNode>
  {
    private final Vector2 position;

    public PositionEqualsPredicate(Vector2 value)
    {
      position = value;
    }

    @Override
    public boolean apply(PathNode pathNode)
    {
      return pathNode.position.equals(position);
    }
  }

  private static class ProjectionPredicate
    implements Predicate<Projection>
  {
    private final Vector2 point;
    private final Vector2 end;
    private final Projection finderProjection;

    public ProjectionPredicate(Projection finder, Vector2 point, Vector2 end)
    {
      this.point = point;
      this.end = end;
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
    public int compare(PathNode first, PathNode second)
    {
      float firstLength = first.getFullLength();
      float secondLength = second.getFullLength();
      return Float.compare(firstLength, secondLength);
    }
  }
}