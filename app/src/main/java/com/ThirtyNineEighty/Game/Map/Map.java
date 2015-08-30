package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.System.GameContext;
import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Map
{
  public final static int StateInProgress = 0;
  public final static int StateWin = 1;
  public final static int StateLose = 2;

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
  private final static PathLengthComparator pathLengthComparator = new PathLengthComparator();

  private final HashMap<String, Projection> projectionsCache;

  private int state;

  public final String name;
  public final MapDescription description;

  public Map(String name, MapDescription description)
  {
    this.name = name;
    this.description = description;
    this.projectionsCache = new HashMap<>();
    this.state = 0;
  }

  public int getState() { return state; }
  public void setState(int value)
  {
    state = value;
    if (state == StateWin)
    {
      if (description.openingMaps != null)
        for (String name : description.openingMaps)
          GameContext.gameProgress.openMap(name);

      if (description.openingTanks != null)
        for (String name : description.openingTanks)
          GameContext.gameProgress.openTank(name);

      if (description.openingUpgrades != null)
        for (String name : description.openingUpgrades)
          GameContext.gameProgress.openUpgrade(name);
    }
  }

  public boolean canMove(GameObject object)
  {
    Vector2 position = Vector.getInstance(2, object.getPosition());
    Vector3 angles = object.getAngles();
    GameDescription description = object.getDescription();

    Projection projection = getProjection(object);
    if (projection == null)
      return true;

    float distance = projection.getRadius() + description.getSpeed() * GameContext.getDelta();
    Vector2 checkPoint = position.getMove(distance, angles.getZ());
    ArrayList<Projection> projections = getProjections(object);

    for (Projection current : projections)
      if (current.contains(checkPoint, projection.getRadius()))
        return false;

    return true;
  }

  public ArrayList<Vector2> findPath(WorldObject finder, WorldObject target)
  {
    Vector2 finderPosition = Vector.getInstance(2, finder.getPosition());
    Vector2 targetPosition = Vector.getInstance(2, target.getPosition());

    float finderRadius = 0;
    Projection finderProjection = getProjection(finder);
    if (finderProjection != null)
      finderRadius = finderProjection.getRadius();

    return findPath(finderPosition, targetPosition, finderRadius, getProjections(finder, target));
  }

  public ArrayList<Vector2> findPath(Vector2 start, Vector2 end, float finderRadius, ArrayList<Projection> projections)
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

        if (projections != null && any(projections, new ProjectionPredicate(finderRadius, nextPoint, end)))
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

  private ArrayList<Projection> getProjections(WorldObject finder) { return getProjections(finder, null); }
  private ArrayList<Projection> getProjections(WorldObject finder, WorldObject target)
  {
    ArrayList<Projection> result = new ArrayList<>();
    ArrayList<WorldObject> objects = new ArrayList<>();

    IWorld world = GameContext.content.getWorld();
    world.getObjects(objects);

    for (WorldObject object : objects)
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

  private Projection getProjection(WorldObject object)
  {
    Vector2 position = Vector.getInstance(2, object.getPosition());
    Projection projection = projectionsCache.get(object.getName());
    if (projection != null)
    {
      projection.setPosition(position);
      return projection;
    }

    projection = Projection.FromObject(object);
    if (projection == null)
      return null;

    projectionsCache.put(object.getName(), projection);
    projection.setPosition(position);
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
      if (Math.abs(value) > description.size)
        result.set(i, Math.signum(value) * description.size);
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
  private final float finderRadius;

  public ProjectionPredicate(float finderRadius, Vector2 point, Vector2 end)
  {
    this.point = point;
    this.end = end;
    this.finderRadius = finderRadius;
  }

  @Override
  public boolean apply(Projection projection)
  {
    return projection.contains(point, finderRadius)
       && !projection.contains(end, finderRadius);
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

