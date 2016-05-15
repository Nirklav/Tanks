package com.ThirtyNineEighty.Base;

import java.util.ArrayList;

public class EngineObjectHost<T extends IEngineObject>
  extends EngineObject
{
  private static final long serialVersionUID = 1L;

  private final ArrayList<T> objects = new ArrayList<>();

  public void add(T object)
  {
    if (object.isInitialized())
      throw new IllegalArgumentException("EngineObject should be not initialized");

    if (object.isEnabled())
      throw new IllegalArgumentException("EngineObject should be disabled");

    if (isInitialized())
      object.initialize();

    if (isEnabled())
      object.enable();

    synchronized (objects)
    {
      objects.add(object);
    }
  }

  public void remove(T object)
  {
    if (object.isEnabled())
      object.disable();

    if (object.isInitialized())
      object.uninitialize();

    synchronized (objects)
    {
      objects.remove(object);
    }
  }

  @Override
  public void initialize()
  {
    super.initialize();

    for (IEngineObject object : getObjectsCopy())
      object.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    for (IEngineObject object : getObjectsCopy())
      object.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    for (IEngineObject object : getObjectsCopy())
      object.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    for (IEngineObject object : getObjectsCopy())
      object.disable();
  }

  private ArrayList<T> getObjectsCopy()
  {
    synchronized (objects)
    {
      return new ArrayList<>(objects);
    }
  }
}
