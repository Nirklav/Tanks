package com.ThirtyNineEighty.Game.Collide;

import com.ThirtyNineEighty.Game.Objects.IGameObject;
import com.ThirtyNineEighty.Game.Objects.IPhysicalObject;

import java.util.Collection;

public class CollideManager
{
  public void move(IGameObject object, Collection<IGameObject> other, float length)
  {
    object.onMoved(length);
  }

  public void rotate(IGameObject object, Collection<IGameObject> other, float angleX, float angleY, float angleZ)
  {
    object.onRotates(angleX, angleY, angleZ);
  }

  private Collision3D check(IGameObject first, IGameObject second)
  {
    IPhysicalObject firstPh = first.getPhysicalModel();
    IPhysicalObject secondPh = second.getPhysicalModel();

    return new Collision3D(firstPh, secondPh);
  }
}
