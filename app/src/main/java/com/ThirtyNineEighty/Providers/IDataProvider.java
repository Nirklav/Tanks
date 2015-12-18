package com.ThirtyNineEighty.Providers;

import java.io.Serializable;

public interface IDataProvider<TData extends Serializable >
  extends Serializable
{
  void set();
  TData get();
}
