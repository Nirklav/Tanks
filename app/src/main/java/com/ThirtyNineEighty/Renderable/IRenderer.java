package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.System.IRenderable;

public interface IRenderer
{
  void add(IRenderable renderable);
  void remove(IRenderable renderable);
}
