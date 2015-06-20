package com.ThirtyNineEighty.Resources.Sources;

public interface ISource<TResource>
{
  String getName(); // Name for cache. If field equals null cache will not work.

  TResource load();
  void reload(TResource resource);
  void release(TResource resource);
}
