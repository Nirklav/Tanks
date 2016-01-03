package com.ThirtyNineEighty.Base.Renderable.Common;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Providers.DataProvider;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.View;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.Serializable;

public class Light
  extends View
{
  private static final long serialVersionUID = 1L;

  private IDataProvider<Data> provider;

  public Light(final Setter setter)
  {
    this.provider = new DataProvider<Data>(Data.class)
    {
      private static final long serialVersionUID = 1L;

      @Override
      public void set(Data data)
      {
        setter.set(data);
      }
    };
  }

  @Override
  public IDataProvider getProvider()
  {
    return provider;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    GameContext.renderer.setLightProvider(provider);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.setLightProvider(null);

    super.uninitialize();
  }

  public static class Data
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final Vector3 Position = new Vector3();
  }

  public abstract static class Setter
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public abstract void set(Data light);
  }
}
