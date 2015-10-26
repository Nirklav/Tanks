package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.RendererContext;

public abstract class GLRenderable<TData extends GLRenderable.Data>
  extends Renderable
{
  protected float[] modelProjectionViewMatrix;
  protected float[] modelMatrix;

  private IDataProvider<TData> provider;

  protected GLRenderable(IDataProvider<TData> provider)
  {
    this.provider = provider;

    modelMatrix = new float[16];
    modelProjectionViewMatrix = new float[16];
  }

  @Override
  public final void draw(RendererContext context)
  {
    TData data = provider.get();

    setModelMatrix(data);
    draw(context, data);
  }

  protected abstract void draw(RendererContext context, TData data);

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

    Vector.release(localPosition);
    Vector.release(localAngles);
  }

  public static class Data
  {
    public Vector3 position;
    public Vector3 angles;

    public Vector3 localPosition;
    public Vector3 localAngles;

    public float scale;

    public Data()
    {
      position = Vector.getInstance(3);
      angles = Vector.getInstance(3);

      localPosition = Vector.getInstance(3);
      localAngles = Vector.getInstance(3);

      scale = 1;
    }
  }
}
