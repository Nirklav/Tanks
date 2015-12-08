package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.IEngineObject;

public interface ISubprogram
  extends IEngineObject
{
  void setBindable(IBindable bindable);
  void update();
}
