package com.ThirtyNineEighty.Base.Providers;

import java.io.Serializable;

public interface IDataProvider<TData extends Serializable>
  extends Serializable
{
  void enqueue(String event);

  void set();
  TData get();
}
