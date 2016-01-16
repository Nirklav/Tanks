package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Resources.Entities.IResource;

public interface ISource<TResource extends IResource>
{
  String getName();

  TResource load();
  void reload(TResource resource);
  void release(TResource resource);
}
