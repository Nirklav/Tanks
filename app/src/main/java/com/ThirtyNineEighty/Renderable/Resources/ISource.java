package com.ThirtyNineEighty.Renderable.Resources;

public interface ISource<TResource>
{
  String getName(); // name for cache

  TResource load();
  void reload(TResource resource);
  void release(TResource resource);
}
