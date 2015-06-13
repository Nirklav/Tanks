package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseWorld
  implements IWorld
{
  private volatile boolean initialized;

  protected final ArrayList<EngineObject> objects;
  protected EngineObject player;

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
    ArrayList<EngineObject> disposed = new ArrayList<>();
    synchronized (objects)
    {
      for (EngineObject object : objects)
        disposed.add(object);
      objects.clear();
    }

    for (EngineObject object : disposed)
      object.dispose();

    initialized = false;
  }

  @Override
  public void enable()
  {
    ArrayList<EngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (EngineObject object : enabling)
      object.enable();
  }

  @Override
  public void disable()
  {
    ArrayList<EngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (EngineObject object : enabling)
      object.disable();
  }

  @Override
  public void fillRenderable(List<I3DRenderable> filled)
  {
    synchronized (objects)
    {
      for (EngineObject engineObject : objects)
      {
        engineObject.setGlobalRenderablePosition();
        Collections.addAll(filled, engineObject.getRenderables());
      }
    }
  }

  @Override
  public void fillObjects(List<EngineObject> filled)
  {
    synchronized (objects)
    {
      for (EngineObject object : objects)
        filled.add(object);
    }
  }

  @Override
  public EngineObject getPlayer() { return player; }

  @Override
  public void add(EngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.add(engineObject);
    }
  }

  @Override
  public void remove(EngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.remove(engineObject);
    }
    engineObject.dispose();
  }
}
