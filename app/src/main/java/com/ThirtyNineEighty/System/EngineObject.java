package com.ThirtyNineEighty.System;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EngineObject
  implements IEngineObject
{
  private static int lastId = 1;
  private static final String generatedNameTemplate = "object_";
  private static final Set<String> usedNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

  private final String name;
  private volatile boolean initialized;
  private volatile boolean enabled;

  protected EngineObject()
  {
    this(null);
  }

  protected EngineObject(String name)
  {
    this.name = getName(name);
  }

  private static String getName(String name)
  {
    if (name != null)
    {
      if (usedNames.contains(name))
        throw new IllegalArgumentException("This name already used");
      return name;
    }

    // TODO: optimization
    while (true)
    {
      name = generatedNameTemplate + Integer.toString(lastId++);
      if (!usedNames.contains(name))
        return name;
    }
  }

  @Override
  public String getName() { return name; }

  @Override
  public boolean isInitialized() { return initialized; }

  @Override
  public boolean isEnabled() { return enabled; }

  @Override
  public void initialize()
  {
    if (initialized)
      throw new IllegalStateException("Object already initialized");

    initialized = true;
  }

  @Override
  public void uninitialize()
  {
    if (!initialized)
      throw new IllegalStateException("Object not initialized");

    if (enabled)
      throw new IllegalStateException("Before call uninitialize you should call disable.");

    initialized = false;
  }

  @Override
  public void enable()
  {
    if (enabled)
      throw new IllegalStateException("Object already enabled");

    if (!initialized)
      throw new IllegalStateException("Before call enable you should call initialize.");

    enabled = true;
  }

  @Override
  public void disable()
  {
    if (!initialized)
      throw new IllegalStateException("Object not initialized");

    if (!enabled)
      throw new IllegalStateException("Object not enabled");

    enabled = false;
  }
}
