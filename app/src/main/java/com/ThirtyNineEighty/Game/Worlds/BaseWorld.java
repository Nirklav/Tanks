package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.System.Bindable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseWorld
  extends Bindable
  implements IWorld
{
  protected final HashMap<String, WorldObject> objects;
  protected WorldObject player;

  protected BaseWorld()
  {
    objects = new HashMap<>();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    ArrayList<WorldObject> disposed;
    synchronized (objects)
    {
      disposed = new ArrayList<>(objects.values());
      objects.clear();
    }

    for (WorldObject object : disposed)
      object.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    ArrayList<WorldObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (WorldObject object : enabling)
      object.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    ArrayList<WorldObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (WorldObject object : enabling)
      object.disable();
  }

  @Override
  public void fillObjects(List<WorldObject> filled)
  {
    synchronized (objects)
    {
      for (WorldObject object : objects.values())
        filled.add(object);
    }
  }

  @Override
  public WorldObject getPlayer() { return player; }

  @Override
  public void add(WorldObject worldObject)
  {
    synchronized (objects)
    {
      objects.put(worldObject.getName(), worldObject);
    }
    worldObject.initialize();
  }

  @Override
  public void remove(WorldObject worldObject)
  {
    synchronized (objects)
    {
      objects.remove(worldObject.getName());
    }
    worldObject.uninitialize();
  }
}
