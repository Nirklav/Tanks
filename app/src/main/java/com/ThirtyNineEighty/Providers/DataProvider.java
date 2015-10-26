package com.ThirtyNineEighty.Providers;

public abstract class DataProvider<TData, TDescription>
  implements IDataProvider<TData>
{
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
