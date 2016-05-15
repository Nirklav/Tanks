package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Renderable.Renderable;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;

public abstract class GLRenderable<TData extends GLRenderable.Data>
  extends Renderable
{
  private static final long serialVersionUID = 1L;

  protected float[] modelProjectionViewMatrix;
  protected float[] modelNormalMatrix;
  protected float[] modelMatrix;

  private IDataProvider<TData> provider;
  private Data prevData;

  protected GLRenderable(IDataProvider<TData> provider)
  {
    this.provider = provider;

    modelMatrix = new float[16];
    modelNormalMatrix = new float[9];
    modelProjectionViewMatrix = new float[16];
  }

  @Override
  public boolean isVisible()
  {
    TData data = provider.get();
    return data.visible;
  }

  @Override
  public final void draw(RendererContext context)
  {
    TData data = provider.get();

    setModelMatrix(data);
    setNormalMatrix();
    draw(context, data);
  }

  protected abstract void draw(RendererContext context, TData data);

  @Override
  public IDataProvider getProvider()
  {
    return provider;
  }

  protected Vector3 getLocalPosition()
  {
    return Vector3.zero;
  }

  protected Vector3 getLocalAngles()
  {
    return Vector3.zero;
  }

  private void setModelMatrix(TData data)
  {
    if (prevData != null && prevData.equals(data))
      return;
    else
    {
      prevData = new Data();
      prevData.setFrom(data);
    }

    // reset matrix
    Matrix.setIdentityM(modelMatrix, 0);

    Vector3 localPosition = data.localPosition.getSum(getLocalPosition());
    Vector3 localAngles = data.localAngles.getSum(getLocalAngles());

    // set global
    Matrix.translateM(modelMatrix, 0, data.position.getX(), data.position.getY(), data.position.getZ());
    Matrix.rotateM(modelMatrix, 0, data.angles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, data.angles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, data.angles.getZ(), 0, 0, 1);

    // set local
    Matrix.translateM(modelMatrix, 0, localPosition.getX(), localPosition.getY(), localPosition.getZ());
    Matrix.rotateM(modelMatrix, 0, localAngles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, localAngles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, localAngles.getZ(), 0, 0, 1);

    // set scale
    Matrix.scaleM(modelMatrix, 0, data.scale, data.scale, data.scale);

    Vector3.release(localPosition);
    Vector3.release(localAngles);
  }

  private void setNormalMatrix()
  {
    for (int x = 0; x < 3; x++)
      for (int y = 0; y < 3; y++)
        modelNormalMatrix[x + y * 3] = modelMatrix[x + y * 3];
  }

  public static class Data
    extends Renderable.Data
  {
    private static final long serialVersionUID = 1L;

    public Vector3 position;
    public Vector3 angles;

    public Vector3 localPosition;
    public Vector3 localAngles;

    public float scale;

    public Data()
    {
      position = Vector3.getInstance();
      angles = Vector3.getInstance();

      localPosition = Vector3.getInstance();
      localAngles = Vector3.getInstance();

      scale = 1;
    }

    public void setFrom(Data data)
    {
      position.setFrom(data.position);
      angles.setFrom(data.angles);
      localPosition.setFrom(data.localPosition);
      localAngles.setFrom(data.localAngles);
      scale = data.scale;
    }

    @Override
    public boolean equals(Object o)
    {
      if (o == null)
        return false;
      if (o == this)
        return true;
      if (o instanceof Data)
      {
        Data otherData = (Data) o;
        return position.equals(otherData.position)
          && angles.equals(otherData.angles)
          && localPosition.equals(otherData.localPosition)
          && localAngles.equals(otherData.localAngles)
          && Float.compare(scale, otherData.scale) == 0;
      }
      return false;
    }
  }
}
