package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Plane;
import com.ThirtyNineEighty.Helpers.Rectangle;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.System.GameContext;
import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Map
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

  public final static float stepSize = 1;

  private final static PathLengthComparator pathLengthComparator = new PathLengthComparator();

  public final float size;

  public Map(float size)
  {
    this.size = size;
  }

  public ArrayList<Vector2> findPath(IEngineObject finder, IEngineObject target)
  {
    Vector2 finderPosition = Vector.getInstance(2, finder.getPosition());
    Vector2 targetPosition = Vector.getInstance(2, target.getPosition());

    ICollidable finderCollidable = finder.getCollidable();
    ICollidable targetCollidable = target.getCollidable();

    float radius = Math.max(finderCollidable.getRadius(), targetCollidable.getRadius());
    Rectangle rect = new Rectangle(finderPosition, targetPosition, radius);
    return findPath(finderPosition, targetPosition, getProjections(rect, target));
  }

  public ArrayList<Vector2> findPath(Vector2 start, Vector2 end, ArrayList<Projection> projections)
  {
    normalizePoint(start);
    normalizePoint(end);

    ArrayList<PathNode> closedSet = new ArrayList<>();
    ArrayList<PathNode> openSet = new ArrayList<>();
    openSet.add(new PathNode(start, 0, getPathLength(start, end)));

    while (openSet.size() > 0)
    {
      PathNode currentNode = Collections.min(openSet, pathLengthComparator);
      if (currentNode.position.equals(end))
        return getPath(currentNode);

      openSet.remove(currentNode);
      closedSet.add(currentNode);

      for (int i = 0; i < DirectionsCount; i++)
      {
        Vector2 nextPoint = getNextPoint(currentNode.position, i);

        if (projections != null && any(projections, new ProjectionPredicate(nextPoint, end)))
          continue;

        float estimatedLength = getPathLength(nextPoint, end);
        PathNode nextNode = new PathNode(currentNode, nextPoint, currentNode.lengthFromStart + stepSize, estimatedLength);

        if (any(closedSet, new PositionEqualsPredicate(nextNode.position)))
          continue;

        PathNode openNode = find(openSet, new PositionEqualsPredicate(nextNode.position));

        if (openNode == null)
          openSet.add(nextNode);
        else
          if (openNode.lengthFromStart > nextNode.lengthFromStart)
          {
            openNode.from = currentNode;
            openNode.lengthFromStart = nextNode.lengthFromStart;
          }
      }
    }

    return null;
  }

  private ArrayList<Projection> getProjections(Rectangle rect, IEngineObject target)
  {
    IWorld world = GameContext.content.getWorld();

    ArrayList<IEngineObject> objects = new ArrayList<>();
    ArrayList<Projection> projections = new ArrayList<>();

    world.fillObjects(objects);

    Vector2 position = Vector.getInstance(2);

    for (IEngineObject object : objects)
    {
      if (target == object)
        continue;

      position.setFrom(object.getPosition());
      if (!rect.contains(position))
        continue;

      Projection projection = Projection.FromObject(object);
      if (projection == null)
        continue;

      projections.add(projection);
    }

    Vector.release(position);
    return projections;
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

  private Vector2 getNextPoint(Vector2 current, int direction)
  {
    Vector2 result = Vector.getInstance(2);
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
      if (Math.abs(value) > size)
        result.set(i, Math.signum(value) * size);
    }

    return result;
  }

  private static ArrayList<Vector2> getPath(PathNode node)
  {
    ArrayList<Vector2> result = new ArrayList<>();
    PathNode currentNode = node;

    Vector2 prevPosition = null;
    Vector2 lastDirection = Vector.getInstance(2);
    Vector2 direction = Vector2.getInstance(2);

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

    Vector.release(lastDirection);
    Vector.release(direction);

    Collections.reverse(result);
    return result;
  }

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
}

class PositionEqualsPredicate implements Predicate<PathNode>
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

class ProjectionPredicate implements Predicate<Projection>
{
  private final Vector2 point;
  private final Vector2 end;

  public ProjectionPredicate(Vector2 point, Vector2 end)
  {
    this.point = point;
    this.end = end;
  }

  @Override
  public boolean apply(Projection projection)
  {
    return projection.contains(point) && !projection.contains(end);
  }
}

class PathLengthComparator
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

class PathNode
{
  public PathNode from;
  public Vector2 position;
  public float lengthFromStart;
  public float estimateLeftLength;

  public PathNode(Vector2 position, float lengthFromStart, float estimateLeftLength)
  {
    this(null, position, lengthFromStart, estimateLeftLength);
  }

  public PathNode(PathNode from, Vector2 position, float lengthFromStart, float estimateLeftLength)
  {
    this.from = from;
    this.position = position;
    this.lengthFromStart = lengthFromStart;
    this.estimateLeftLength = estimateLeftLength;
  }

  public float getFullLength() { return lengthFromStart + estimateLeftLength; }
}

class Projection
{
  private static final Plane plane = new Plane();
  private final float radius;
  private final Vector2 position;

  public static Projection FromObject(IEngineObject object)
  {
    ICollidable collidable = object.getCollidable();
    if (collidable == null)
      return null;

    Vector2 position = Vector.getInstance(2, object.getPosition());
    ArrayList<Vector2> vertices = collidable.getConvexHull(plane);

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

    Vector.release(tempVector);
    return new Projection(radius, position);
  }

  private Projection(float radius, Vector2 position)
  {
    this.radius = radius;
    this.position = position;
  }

  public boolean contains(Vector2 vector)
  {
    Vector2 tempVector = Vector.getInstance(2, vector);
    tempVector.subtract(position);
    return radius > tempVector.getLength();
  }
}
