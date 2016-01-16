package com.ThirtyNineEighty.Base.Providers;

import com.ThirtyNineEighty.Base.Renderable.Renderable;

public class RenderableDataProvider<TData extends Renderable.Data>
  extends DataProvider<TData>
{
  private boolean visible = true;

  public RenderableDataProvider(Class<TData> dataClass)
  {
    super(dataClass);
  }

  @Override
  public void set(TData data)
  {
    data.visible = visible;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean value)
  {
    visible = value;
  }
}
