package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Rectangle;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;
import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Map
{
  private final static int Top = 0;
  private final static int Bottom = 1;
  private final static int Left = 2;
  private final static int Right = 3;

  private final static float stepSize = 1;

  private final static PathLengthComparator pathLengthComparator = new PathLengthComparator();

  public final float size;
  public final ArrayList<IEngineObject> objects;

  public Map(float size)
  {
    this.size = size;
    this.objects = new ArrayList<>();
  }

  public ArrayList<Vector2> findPath(Vector2 start, Vector2 end)
  {
    IWorld world = GameContext.content.getWorld();

    objects.clear();
    world.fillObjects(objects);

    Rectangle rect = new Rectangle(start, end);
    Vector2 position = Vector.getInstance(2);

    int size = objects.size();
    for (int i = 0; i < size; i++)
    {
      IEngineObject object = objects.get(i);
      Vector3 objPosition = object.getPosition();
      position.setFrom(objPosition);

      if (!rect.contains(position))
        objects.remove(i);
    }

    ArrayList<PathNode> closedSet = new ArrayList<>();
    ArrayList<PathNode> openSet = new ArrayList<>();
    openSet.add(new PathNode(start, 0, getPathLength(start, end)));

    while (openSet.size() > 0)
    {
      PathNode currentNode = Collections.min(openSet, pathLengthComparator);
      if (currentNode.position.equals(end))
        return getPathForNode(currentNode);

      openSet.remove(currentNode);
      closedSet.add(currentNode);

      for (int i = 0; i < 4; i++)
      {
        Vector2 nextPoint = getNextPoint(currentNode.position, i);

        // TODO: check all objects
        //if (objects.Any(obj => obj.Position == nextPoint && obj.Position != end))
        //  continue;

        PathNode neighbourNode = new PathNode(currentNode, nextPoint, currentNode.lengthFromStart + stepSize, getPathLength(currentNode.position, end));

        if (any(closedSet, new PositionEqualsPredicate(neighbourNode.position)))
          continue;

        PathNode openNode = find(openSet, new PositionEqualsPredicate(neighbourNode.position));

        if (openNode == null)
          openSet.add(neighbourNode);
        else
          if (openNode.lengthFromStart > neighbourNode.lengthFromStart)
          {
            openNode.from = currentNode;
            openNode.lengthFromStart = neighbourNode.lengthFromStart;
          }
      }
    }

    return null;
  }

  private static Vector2 getNextPoint(Vector2 current, int direction)
  {
    Vector2 result = Vector.getInstance(2);
    switch (direction)
    {
    case Top: result.setFrom(current.getX(), current.getY() + stepSize); break;
    case Left: result.setFrom(current.getX() - stepSize, current.getY()); break;
    case Right: result.setFrom(current.getX() + stepSize, current.getY()); break;
    case Bottom: result.setFrom(current.getX(), current.getY() - stepSize); break;
    }

    return result;
  }

  private static ArrayList<Vector2> getPathForNode(PathNode pathNode)
  {
    ArrayList<Vector2> result = new ArrayList<>();
    PathNode currentNode = pathNode;

    while (currentNode != null)
    {
      result.add(currentNode.position);
      currentNode = currentNode.from;
    }

    Collections.reverse(result);
    return result;
  }

  private static float getPathLength(Vector2 start, Vector2 end)
  {
    float x = Math.abs(start.getX() - end.getX());
    float y = Math.abs(start.getX() - end.getY());
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

  private static class PositionEqualsPredicate implements Predicate<PathNode>
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

  private static class PathLengthComparator
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

  private static class PathNode
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
}
