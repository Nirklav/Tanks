package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Renderable.IRenderable;

public interface IBindable
{
  boolean isInitialized();

  void initialize();
  void uninitialize();

  void enable();
  void disable();

  void bind(ISubprogram subprogram);
  void unbind(ISubprogram subprogram);

  void bind(IRenderable renderable);
  void unbind(IRenderable renderable);
}
