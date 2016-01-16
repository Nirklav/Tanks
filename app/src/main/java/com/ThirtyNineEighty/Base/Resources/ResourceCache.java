package com.ThirtyNineEighty.Base.Resources;

import com.ThirtyNineEighty.Base.Resources.Entities.IResource;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceCache<TResource extends IResource>
{
  private final String name;
  private final HashMap<String, ResourceHolder<TResource>> cache = new HashMap<>();

  public ResourceCache(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public TResource get(ISource<TResource> source)
  {
    if (source == null)
      throw new NullPointerException("source is null");

    String cacheName = source.getName();
    if (cacheName == null)
      throw new IllegalStateException("Source name must be not null");

    ResourceHolder<TResource> container = cache.get(cacheName);
    if (container == null)
      cache.put(cacheName, container = new ResourceHolder<>(source));

    return container.get();
  }

  public void release(TResource resource)
  {
    ResourceHolder<TResource> holder = getHolder(resource);
    holder.release();
  }

  public void reload()
  {
    for (ResourceHolder<TResource> holder : cache.values())
      holder.reload();
  }

  public int clearUnused()
  {
    ArrayList<String> removing = null;

    for (ResourceHolder<TResource> holder : cache.values())
    {
      boolean cleared = holder.tryClear();

      if (cleared)
      {
        if (removing == null)
          removing = new ArrayList<>();
        removing.add(holder.getName());
      }
    }

    if (removing == null)
      return 0;

    for (String resourceName : removing)
      cache.remove(resourceName);
    return removing.size();
  }

  public void clear()
  {
    for(ResourceHolder<TResource> holder : cache.values())
      holder.clear();

    cache.clear();
  }

  public int getSize()
  {
    return cache.size();
  }

  private ResourceHolder<TResource> getHolder(TResource resource)
  {
    if (resource == null)
      throw new NullPointerException("resource is null");

    String name = resource.getName();
    if (name == null)
      throw new IllegalStateException("For reloading resource must have cache name");

    ResourceHolder<TResource> container = cache.get(name);
    if (container == null)
      throw new IllegalStateException(String.format("Cache does not contain resource with %s name", name));

    return container;
  }
}

