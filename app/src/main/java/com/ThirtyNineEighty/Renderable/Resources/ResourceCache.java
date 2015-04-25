package com.ThirtyNineEighty.Renderable.Resources;

import java.util.HashMap;

public class ResourceCache<TResource>
{
  private HashMap<String, Container<TResource>> cache = new HashMap<String, Container<TResource>>();

  public TResource get(ISource<TResource> source)
  {
    String cacheName = source.getName();
    Container<TResource> container = cache.get(cacheName);
    if (container != null)
      return container.resource;

    TResource resource = source.load();
    cache.put(cacheName, new Container<TResource>(resource, source));
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
