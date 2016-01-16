package com.ThirtyNineEighty.Base;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public abstract class EngineObject
  implements IEngineObject
{
  private static final long serialVersionUID = 1L;

  private static AtomicLong lastId = new AtomicLong(System.currentTimeMillis());

  private final Long id;

  private volatile boolean initialized;
  private volatile boolean enabled;

  protected EngineObject()
  {
    id = lastId.getAndIncrement();
  }

  private void writeObject(java.io.ObjectOutputStream stream)
    throws IOException
  {
    if (initialized)
      throw new IllegalStateException("Can't serialize initialized object");

    stream.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();

    if (initialized)
      throw new IllegalStateException("Deserialized initialized object. This is not right.");
  }

  @Override
  public Long getId()
  {
    return id;
  }

  @Override
  public boolean isInitialized()
  {
    return initialized;
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

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
