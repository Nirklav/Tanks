package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable;

public abstract class GLBase
  extends Renderable
{
  protected float[] modelProjectionViewMatrix;
  protected float[] modelMatrix;

  protected ILocationProvider<Vector3> locationProvider;
  protected Location<Vector3> lastLocation;

  protected float scale;

  protected GLBase(ILocationProvider<Vector3> provider)
  {
    modelMatrix = new float[16];
    modelProjectionViewMatrix = new float[16];
    scale = 1f;

    locationProvider = provider;
  }

  protected void setModelMatrix()
  {
    Location<Vector3> loc = locationProvider.getLocation();
    if (lastLocation != null && lastLocation.equals(loc))
      return;

    lastLocation = loc;

    // reset matrix
    Matrix.setIdentityM(modelMatrix, 0);

    // set global
    Matrix.translateM(modelMatrix, 0, loc.position.getX(), loc.position.getY(), loc.position.getZ());
    Matrix.rotateM(modelMatrix, 0, loc.angles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, loc.angles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, loc.angles.getZ(), 0, 0, 1);

    // set local
    Matrix.translateM(modelMatrix, 0, loc.localPosition.getX(), loc.localPosition.getY(), loc.localPosition.getZ());
    Matrix.rotateM(modelMatrix, 0, loc.localAngles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, loc.localAngles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, loc.localAngles.getZ(), 0, 0, 1);

    // set scale
    Matrix.scaleM(modelMatrix, 0, scale, scale, scale);
  }

  public void setScale(float value)
  {
    scale = value;
  }
}
