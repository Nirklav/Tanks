package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.TanksContext;

import java.util.ArrayList;

public class Path
  implements IPath
{
  private static final long serialVersionUID = 1L;

  private static final float defaultStepCompletion = 1.0f;

  private final WorldObject<?, ?> object;
  private final ArrayList<Vector2> path;
  private final float stepCompletion;

  private int currentPathStep;

  public Path(WorldObject<?, ?> object, ArrayList<Vector2> path)
  {
    this.object = object;
    this.path = path;
    this.stepCompletion = object.collidable != null
      ? object.collidable.getRadius()
      : defaultStepCompletion;
  }

  @Override
  public float distance()
  {
    float distance = 0.0f;

    Vector3 start = start();
    Vector3 end = end();

    if (start != null && end != null)
    {
      Vector3 vec = end.getSubtract(start);
      distance = vec.getLength();
      Vector.release(vec);
    }

    Vector.release(start);
    Vector.release(end);
    return distance;
  }

  @Override
  public Vector3 start()
  {
    int size = path.size();
    if (size < 1)
      return null;
    return Vector.getInstance(3, path.get(0));
  }

  @Override
  public Vector3 end()
  {
    int size = path.size();
    if (size < 1)
      return null;
    return Vector.getInstance(3, path.get(size - 1));
  }

  public boolean moveObject()
  {
    Vector2 movingVector = getMovingVector();
    if (movingVector == null)
      return false;

    IWorld world = TanksContext.content.getWorld();
    IMap map = world.getMap();

    float movingAngle = Vector2.xAxis.getAngle(movingVector);
    Vector3 targetAngles = Vector.getInstance(3, 0, 0, movingAngle);
    Vector3 objectAngles = object.getAngles();

    object.rotateTo(targetAngles);
    Vector.release(targetAngles);

    if (Math.abs(objectAngles.getZ() - movingAngle) >= 15)
      return true;

    // Try move
    if (map.canMove(object))
    {
      object.move();
      return true;
    }

    return false;
  }

  private Vector2 getMovingVector()
  {
    Vector2 objectPosition = Vector.getInstance(2, object.getPosition());
    Vector2 nextStepVector = Vector.getInstance(2);

    while (true)
    {
      // We arrived
      if (currentPathStep >= path.size())
      {
        Vector.release(objectPosition);
        return null;
      }

      Vector2 nextStep = path.get(currentPathStep);
      nextStepVector.setFrom(nextStep);
      nextStepVector.subtract(objectPosition);

      if (nextStepVector.getLength() > stepCompletion)
      {
        Vector.release(objectPosition);
        return nextStepVector;
      }

      currentPathStep++;
    }
  }

  @Override
  public void release()
  {
    Vector.release(path);
  }
}
