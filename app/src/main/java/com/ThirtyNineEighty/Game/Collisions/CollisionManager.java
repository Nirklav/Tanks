package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.Collection;

public class CollisionManager
{
  public void move(IEngineObject object, Collection<IEngineObject> objects, float length)
  {
    object.onMoved(length);

    resolve(object, objects);
  }

  public void rotate(IEngineObject object, Collection<IEngineObject> objects, Vector3 angles)
  {
    object.onRotates(angles);

    resolve(object, objects);
  }

  private void resolve(IEngineObject object, Collection<IEngineObject> objects)
  {
    for(IEngineObject current : objects)
    {
      if (object == current)
        continue;

      ICollidable firstPh = object.getCollidable();
      ICollidable secondPh = current.getCollidable();

      if (firstPh.getRadius() + secondPh.getRadius() < getLength(object, current))
        continue;

      Collision3D collision = new Collision3D(firstPh, secondPh);

      if (collision.isCollide())
        object.onMoved(collision.getMTV(), collision.getMTVLength());
    }
  }

  private float getLength(IEngineObject one, IEngineObject two)
  {
    Vector3 positionOne = one.getPosition();
    Vector3 positionTwo = two.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }
}
