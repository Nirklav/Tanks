package com.ThirtyNineEighty.Renderable.Common;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Providers.DataProvider;
import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.View;
import com.ThirtyNineEighty.System.GameContext;

import java.io.Serializable;

public class Camera
  extends View
{
  private static final long serialVersionUID = 1L;

  private IDataProvider<Data> provider;

  public Camera(final Setter setter)
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

    GameContext.renderer.setCameraProvider(provider);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.setCameraProvider(null);

    super.uninitialize();
  }

  public static class Data
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final Vector3 eye = new Vector3();
    public final Vector3 target = new Vector3();
  }

  public abstract static class Setter
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public abstract void set(Data camera);
  }
}
