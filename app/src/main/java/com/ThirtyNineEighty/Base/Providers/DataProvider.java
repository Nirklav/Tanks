package com.ThirtyNineEighty.Base.Providers;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashSet;

public abstract class DataProvider<TData extends Serializable>
  implements IDataProvider<TData>,
             Serializable
{
  private static final long serialVersionUID = 1L;

  private TData renderableOwnedData;  // used only in GLThread
  private TData modelOwnedData;       // used only in UpdateThread (Content)
  private volatile TData transferData;

  private final SyncObject syncObject;
  private volatile boolean dataUpdated;

  private HashSet<String> events;

  protected DataProvider(Class<TData> dataClass)
  {
    this.renderableOwnedData = create(dataClass);
    this.modelOwnedData = create(dataClass);
    this.transferData = create(dataClass);
    this.syncObject = new SyncObject();
    this.events = new HashSet<>();
  }

  private TData create(Class<TData> dataClass)
  {
    try
    {
      Constructor<TData> ctor = dataClass.getConstructor();
      return ctor.newInstance();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public final void enqueue(String event)
  {
    events.add(event);
  }

  @Override
  public void set() // invoked by UpdateThread (Content)
  {
    // Raise events.
    for (String event : events)
      onEvent(event);
    events.clear();

    // Ser data.
    set(modelOwnedData);

    // Swap data.
    synchronized (syncObject)
    {
      TData tmp = transferData;
      transferData = modelOwnedData;
      modelOwnedData = tmp;

      dataUpdated = true;
    }
  }

  @Override
  public final TData get() // invoked by GLThread
  {
    // if we are here then renderableOwnedData already not used by renderable
    synchronized (syncObject)
    {
      if (dataUpdated)
      {
        dataUpdated = false;

        TData tmp = transferData;
        transferData = renderableOwnedData;
        renderableOwnedData = tmp;
      }
    }

    return renderableOwnedData;
  }

  protected void onEvent(String event) { }
  public abstract void set(TData data);

  // For sync object serialization
  private static class SyncObject
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
  }
}
