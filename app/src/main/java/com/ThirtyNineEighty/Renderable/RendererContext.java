package com.ThirtyNineEighty.Renderable;

import android.opengl.Matrix;

import com.ThirtyNineEighty.System.GameContext;

public class RendererContext
{
  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;

  private float[] orthoMatrix;

  private Light light;

  public RendererContext()
  {
    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];

    orthoMatrix = new float[16];
    Matrix.setIdentityM(orthoMatrix, 0);
    Matrix.orthoM(orthoMatrix, 0, GameContext.Left, GameContext.Right, GameContext.Bottom, GameContext.Top, -1, 1);

    light = new Light();
  }

  public float[] getProjectionViewMatrix() { return projectionViewMatrix; }
  public float[] getOrthoMatrix() { return orthoMatrix; }
  public Light getLight() { return light; }

  public void setLight(Light value)
  {
    light = value;
  }

  public void setCamera(Camera camera)
  {
    float eyeX = camera.eye.getX();
    float eyeY = camera.eye.getY();
    float eyeZ = camera.eye.getZ();

    float targetX = camera.target.getX();
    float targetY = camera.target.getY();
    float targetZ = camera.target.getZ();

    Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, 0.0f, 0.0f, 1.0f);
    Matrix.perspectiveM(projectionMatrix, 0, 60.0f, GameContext.getAspect(), 0.1f, 60.0f);
    Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
  }
}
