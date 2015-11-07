package com.ThirtyNineEighty.Providers;

import java.io.Serializable;

public abstract class DataProvider<TData extends Serializable, TDescription extends Serializable>
  implements IDataProvider<TData>,
             Serializable
{
  private static final long serialVersionUID = 1L;

  private final TData data;
  private final TDescription description;

  protected DataProvider(TData data, TDescription description)
  {
    this.data = data;
    this.description = description;
  }

  @Override
  public final TData get()
  {
    set(data, description);
    return data;
  }

  public abstract void set(TData data, TDescription description);
}
