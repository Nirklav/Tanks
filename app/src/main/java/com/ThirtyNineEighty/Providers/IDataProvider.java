package com.ThirtyNineEighty.Providers;

import java.io.Serializable;

public interface IDataProvider<T extends Serializable>
  extends Serializable
{
  T get();
}
