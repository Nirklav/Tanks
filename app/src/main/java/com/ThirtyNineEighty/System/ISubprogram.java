package com.ThirtyNineEighty.System;

public interface ISubprogram
  extends IEngineObject,
          ISaveble
{
  void setBindable(IBindable bindable);
  void update();
}
