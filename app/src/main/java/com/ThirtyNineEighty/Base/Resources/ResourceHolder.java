package com.ThirtyNineEighty.Base.Resources;

import com.ThirtyNineEighty.Base.Resources.Entities.IResource;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;

class ResourceHolder<TResource extends IResource>
{
  private final ISource<TResource> source;
  private TResource resource;
  private int referenceCounter;

  public ResourceHolder(ISource<TResource> source)
  {
    this.source = source;
  }

  public String getName()
  {
    return source.getName();
  }

  public TResource get()
  {
    if (resource == null)
    {
      resource = source.load();
      if (resource == null)
        throw new IllegalStateException("Source should return not null resource");
    }

    referenceCounter++;
    return resource;
  }

  public void reload()
  {
    source.reload(resource);
  }

  public void release()
  {
    referenceCounter --;

    if (referenceCounter < 0)
      throw new IllegalStateException("reference counter < 0");
  }

  public boolean tryClear()
  {
    if (referenceCounter == 0)
    {
      clear();
      return true;
    }

    return false;
  }

  public void clear()
  {
    referenceCounter = 0;

    if (resource != null)
    {
      source.release(resource);
      resource = null;
    }
  }
}
