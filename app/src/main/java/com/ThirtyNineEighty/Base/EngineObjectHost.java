package com.ThirtyNineEighty.Base;

import java.util.ArrayList;

public class EngineObjectHost
  extends EngineObject
{
  private static final long serialVersionUID = 1L;

  private final ArrayList<IEngineObject> objects = new ArrayList<>();

  public void add(IEngineObject object)
  {
    if (object.isInitialized())
      throw new IllegalArgumentException("Bindable should be not initialized");

    if (object.isEnabled())
      throw new IllegalArgumentException("Bindable should be disabled");

    if (isInitialized())
      object.initialize();

    if (isEnabled())
      object.enable();

    synchronized (objects)
    {
      objects.add(object);
    }
  }

  public void remove(IEngineObject object)
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

  private ArrayList<IEngineObject> getObjectsCopy()
  {
    synchronized (objects)
    {
      return new ArrayList<>(objects);
    }
  }
}
