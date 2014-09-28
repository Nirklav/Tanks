package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IGameObject;
import com.ThirtyNineEighty.Game.Objects.ICollidable;

import java.util.Collection;

public class CollideManager
{
  public void move(IGameObject object, Collection<IGameObject> objects, float length)
  {
    object.onMoved(length);

    for(IGameObject current : objects)
    {
      if (object.getId() == current.getId())
        continue;

      Collision3D collision = check(object, current);

      if (collision.isCollide())
        object.onMoved(collision.getMTV(), collision.getMTVLength());
    }
  }

  public void rotate(IGameObject object, Collection<IGameObject> objects, float angleX, float angleY, float angleZ)
  {
    object.onRotates(angleX, angleY, angleZ);

    for(IGameObject current : objects)
    {
      if (object.getId() == current.getId())
        continue;

      Collision3D collision = check(object, current);

      if (collision.isCollide())
        object.onMoved(collision.getMTV(), collision.getMTVLength());
    }
  }

  private Collision3D check(IGameObject first, IGameObject second)
  {
    ICollidable firstPh = first.getPhysicalModel();
    ICollidable secondPh = second.getPhysicalModel();

    return new Collision3D(firstPh, secondPh);
  }
}
