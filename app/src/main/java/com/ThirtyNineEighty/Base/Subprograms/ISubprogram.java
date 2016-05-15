package com.ThirtyNineEighty.Base.Subprograms;

import com.ThirtyNineEighty.Base.IBindable;
import com.ThirtyNineEighty.Base.IEngineObject;

public interface ISubprogram
  extends IEngineObject
{
  void prepare(ITaskAdder adder);

  void setBindable(IBindable bindable);
  void update();
}
