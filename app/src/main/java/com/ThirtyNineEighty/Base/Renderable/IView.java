package com.ThirtyNineEighty.Base.Renderable;

import com.ThirtyNineEighty.Base.IBindable;
import com.ThirtyNineEighty.Base.IEngineObject;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;

public interface IView
  extends IEngineObject
{
  void setBindable(IBindable bindable);

  IDataProvider getProvider();
}
