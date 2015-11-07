package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.System.BindableHost;

import java.util.List;

public abstract class BaseWorld
  extends BindableHost<WorldObject>
  implements IWorld
{
  private static final long serialVersionUID = 1L;

  protected WorldObject<?, ?> player;

  @Override
  public void getObjects(List<WorldObject<?, ?>> filled)
  {
    synchronized (objects)
    {
      for (WorldObject<?, ?> object : objects)
        filled.add(object);
    }
  }

  @Override
  public WorldObject<?, ?> getPlayer() { return player; }

  @Override
  public boolean needSave() { return false; }
}
