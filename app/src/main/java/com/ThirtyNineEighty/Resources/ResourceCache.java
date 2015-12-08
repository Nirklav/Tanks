package com.ThirtyNineEighty.Resources;

import com.ThirtyNineEighty.Resources.Sources.ISource;

import java.util.HashMap;

public class ResourceCache<TResource>
{
  private final String name;
  private final HashMap<String, Container<TResource>> cache = new HashMap<>();

  public ResourceCache(String name)
  {
    this.name = name;
  }

  public String getName() { return name; }

  public TResource get(ISource<TResource> source)
  {
    String cacheName = source.getName();
    if (cacheName != null)
    {
      Container<TResource> container = cache.get(cacheName);
      if (container != null)
        return container.resource;
    }

    TResource resource = source.load();

    if (cacheName != null)
      cache.put(cacheName, new Container<>(resource, source));

    return resource;
  }

  public void reload()
  {
    for (Container<TResource> container : cache.values())
      container.reload();
  }

  public void clear()
  {
    for(Container<TResource> container : cache.values())
      container.release();

    cache.clear();
  }

  public int getSize()
  {
    return cache.size();
  }

  private static class Container<TResource>
  {
    public final TResource resource;
    public final ISource<TResource> source;

    public Container(TResource res, ISource<TResource> src)
    {
      resource = res;
      source = src;
    }

    public void reload()
    {
      source.reload(resource);
    }

    public void release()
    {
      source.release(resource);
    }
  }
}
