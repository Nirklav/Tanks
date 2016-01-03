package com.ThirtyNineEighty.Base.Worlds;

import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.BindableHost;

import java.util.List;

public abstract class BaseWorld
  extends BindableHost<WorldObject>
  implements IWorld
{
  private static final long serialVersionUID = 1L;

  protected WorldObject<?, ?> player;

  @Override
  public IMap getMap()
  {
    return null;
  }

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
  public WorldObject<?, ?> getPlayer()
  {
    return player;
  }

  @Override
  public boolean needSave()
  {
    return false;
  }
}
