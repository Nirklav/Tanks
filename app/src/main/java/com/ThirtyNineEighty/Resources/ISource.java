package com.ThirtyNineEighty.Resources;

public interface ISource<TResource>
{
  String getName(); // Name for cache. If null cache will not work.

  TResource load();
  void reload(TResource resource);
  void release(TResource resource);
}
