package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseWorld
  implements IWorld
{
  private volatile boolean initialized;

  protected final ArrayList<IEngineObject> objects;
  protected IEngineObject player;

  protected BaseWorld()
  {
    objects = new ArrayList<>();
  }

  @Override
  public boolean isInitialized() { return initialized; }

  @Override
  public void initialize(Object args)
  {
    initialized = true;
  }

  @Override
  public void uninitialize()
  {
    ArrayList<IEngineObject> disposed = new ArrayList<>();
    synchronized (objects)
    {
      for (IEngineObject object : objects)
        disposed.add(object);
      objects.clear();
    }

    for (IEngineObject object : disposed)
      object.dispose();

    initialized = false;
  }

  @Override
  public void enable()
  {
    ArrayList<IEngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (IEngineObject object : enabling)
      object.enable();
  }

  @Override
  public void disable()
  {
    ArrayList<IEngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (IEngineObject object : enabling)
      object.disable();
  }

  @Override
  public void fillRenderable(List<I3DRenderable> filled)
  {
    synchronized (objects)
    {
      for (IEngineObject engineObject : objects)
      {
        engineObject.setGlobalRenderablePosition();
        Collections.addAll(filled, engineObject.getRenderables());
      }
    }
  }

  @Override
  public void fillObjects(List<IEngineObject> filled)
  {
    synchronized (objects)
    {
      for (IEngineObject object : objects)
        filled.add(object);
    }
  }

  @Override
  public IEngineObject getPlayer() { return player; }

  @Override
  public void add(IEngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.add(engineObject);
    }
  }

  @Override
  public void remove(IEngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.remove(engineObject);
    }
    engineObject.dispose();
  }
}
