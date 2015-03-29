package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public class CollisionManager
{
  private final Iterable<IEngineObject> worldObjects;
  private final ArrayList<IEngineObject> resolvingObjects;

  // cached list
  private final ArrayList<IEngineObject> collidedObjects;

  public CollisionManager(Iterable<IEngineObject> objects)
  {
    worldObjects = objects;
    collidedObjects = new ArrayList<IEngineObject>();
    resolvingObjects = new ArrayList<IEngineObject>();
  }

  public void move(GameObject object)
  {
    Characteristic c = object.getCharacteristics();
    object.onMoved(c.getSpeed() * GameContext.getDelta());
    addToResolving(object);
  }

  public void move(IEngineObject object, float length)
  {
    object.onMoved(length);
    addToResolving(object);
  }

  public void move(IEngineObject object, Vector3 vector, float length)
  {
    object.onMoved(length, vector);
    addToResolving(object);
  }

  public void rotate(IEngineObject object, Vector3 angles)
  {
    object.onRotates(angles);
    addToResolving(object);
  }

  public void resolve()
  {
    int size = resolvingObjects.size();
    for (int i = size - 1; i >= 0; i--)
    {
      IEngineObject currentObj = resolvingObjects.get(i);
      resolve(currentObj);
    }

    resolvingObjects.clear();
  }

  private void addToResolving(IEngineObject object)
  {
    if (!resolvingObjects.contains(object))
      resolvingObjects.add(object);
  }

  private void resolve(IEngineObject object)
  {
    ICollidable objectPh = object.getCollidable();
    if (objectPh == null)
      return;

    for (IEngineObject current : worldObjects)
    {
      if (object == current)
        continue;

      ICollidable currentPh = current.getCollidable();
      if (currentPh == null)
        continue;

      if (objectPh.getRadius() + currentPh.getRadius() < getLength(object, current))
        continue;

      Collision3D collision = new Collision3D(objectPh, currentPh);

      if (collision.isCollide())
      {
        collidedObjects.add(object);
        object.onMoved(collision.getMTVLength(), collision.getMTV());
      }
    }

    // onCollide can change worldObjects
    for (IEngineObject current : collidedObjects)
      object.onCollide(current);

    collidedObjects.clear();
  }

  private float getLength(IEngineObject one, IEngineObject two)
  {
    Vector3 positionOne = one.getPosition();
    Vector3 positionTwo = two.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }
}
