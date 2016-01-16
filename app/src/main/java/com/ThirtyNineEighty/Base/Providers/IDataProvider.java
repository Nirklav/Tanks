package com.ThirtyNineEighty.Base.Providers;

import java.io.Serializable;

public interface IDataProvider<TData extends Serializable>
  extends Serializable
{
  void set();
  TData get();
}
