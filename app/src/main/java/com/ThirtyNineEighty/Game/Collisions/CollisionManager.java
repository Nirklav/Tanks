package com.ThirtyNineEighty.Game.Collisions;

import com.ThirtyNineEighty.Game.Objects.IGameObject;
import com.ThirtyNineEighty.Helpers.Vector3;

import java.util.Collection;

public class CollisionManager
{
  public void move(IGameObject object, Collection<IGameObject> objects, float length)
  {
    object.onMoved(length);

    resolve(object, objects);
  }

  public void rotate(IGameObject object, Collection<IGameObject> objects, float angleX, float angleY, float angleZ)
  {
    object.onRotates(angleX, angleY, angleZ);

    resolve(object, objects);
  }

  private void resolve(IGameObject object, Collection<IGameObject> objects)
  {
    for(IGameObject current : objects)
    {
      if (object.getId() == current.getId())
        continue;

      if (object.getRadius() + current.getRadius() < getLength(object, current))
        continue;

      ICollidable firstPh = object.getCollidable();
      ICollidable secondPh = current.getCollidable();

      Collision3D collision = new Collision3D(firstPh, secondPh);

      if (collision.isCollide())
        object.onMoved(collision.getMTV(), collision.getMTVLength());
    }
  }

  private float getLength(IGameObject one, IGameObject two)
  {
    Vector3 positionOne = one.getPosition();
    Vector3 positionTwo = two.getPosition();

    Vector3 lengthVector = positionOne.getSubtract(positionTwo);
    return lengthVector.getLength();
  }
}
