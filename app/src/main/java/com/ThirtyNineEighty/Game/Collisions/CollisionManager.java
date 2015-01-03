package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

public class CollisionManager
{
  private final Iterable<IEngineObject> worldObjects;

  public CollisionManager(Iterable<IEngineObject> objects)
  {
    worldObjects = objects;
  }

  public void move(GameObject object)
  {
    Characteristic c = object.getCharacteristics();
    object.onMoved(c.getSpeed() * GameContext.getDelta());
    resolve(object);
  }

  public void move(IEngineObject object, float length)
  {
    object.onMoved(length);
    resolve(object);
  }

  public void move(IEngineObject object, Vector3 vector, float length)
  {
    object.onMoved(vector, length);
    resolve(object);
  }

  public void rotate(IEngineObject object, Vector3 angles)
  {
    object.onRotates(angles);
    resolve(object);
  }

  private void resolve(IEngineObject object)
  {
    for (IEngineObject current : worldObjects)
    {
      if (object == current)
        continue;

      ICollidable firstPh = object.getCollidable();
      ICollidable secondPh = current.getCollidable();

      if (firstPh.getRadius() + secondPh.getRadius() < getLength(object, current))
        continue;

      Collision3D collision = new Collision3D(firstPh, secondPh);

      if (collision.isCollide())
      {
        object.onCollide(current);
        object.onMoved(collision.getMTV(), collision.getMTVLength());
      }
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
