package com.ThirtyNineEighty.Base.Renderable;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Renderable.Common.Camera;
import com.ThirtyNineEighty.Base.Renderable.Common.Light;

public class RendererContext
{
  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;

  private float[] orthoMatrix;

  private Light.Data lightData;

  public RendererContext()
  {
    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];

    orthoMatrix = new float[16];
    Matrix.setIdentityM(orthoMatrix, 0);
    Matrix.orthoM(orthoMatrix, 0, Renderer.Left, Renderer.Right, Renderer.Bottom, Renderer.Top, -1, 1);

    lightData = new Light.Data();
  }

  public float[] getProjectionViewMatrix() { return projectionViewMatrix; }
  public float[] getOrthoMatrix() { return orthoMatrix; }
  public float[] getViewMatrix() { return viewMatrix; }

  public Light.Data getLight() { return lightData; }

  public void setLight(Light.Data value)
  {
    lightData = value;
  }

  public void setCamera(Camera.Data value)
  {
    float eyeX = value.eye.getX();
    float eyeY = value.eye.getY();
    float eyeZ = value.eye.getZ();

    float targetX = value.target.getX();
    float targetY = value.target.getY();
    float targetZ = value.target.getZ();

    Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, 0.0f, 0.0f, 1.0f);
    Matrix.perspectiveM(projectionMatrix, 0, 60.0f, Renderer.getAspect(), 0.1f, 200.0f);
    Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
  }
}
