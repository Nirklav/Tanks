package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Providers.IDataProvider;

public interface IView
  extends IEngineObject
{
  void setBindable(IBindable bindable);

  IDataProvider getProvider();
}
