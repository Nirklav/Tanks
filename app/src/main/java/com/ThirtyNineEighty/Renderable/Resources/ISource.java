package com.ThirtyNineEighty.Renderable.Resources;

public interface ISource<TResource>
{
  TResource load();
  void reload(TResource resource);
}
