package com.ThirtyNineEighty.Base.Providers;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Renderable.GL.GLSprite;

public class GLSpriteProvider
  extends RenderableDataProvider<GLSprite.Data>
{
  private static final long serialVersionUID = 1L;

  private String imageName;
  private Vector3 position;
  private Vector3 colorCoefficients;
  private float width;
  private float height;
  private float angle;

  private float[] modelMatrix;
  private boolean needRebuildMatrix;

  public GLSpriteProvider(String imageName)
  {
    super(GLSprite.Data.class);

    this.imageName = imageName;
    this.modelMatrix = new float[16];
    this.position = Vector3.getInstance();
    this.colorCoefficients = Vector3.getInstance(1, 1, 1);
    this.width = 1;
    this.height = 1;
  }

  @Override
  public void set(GLSprite.Data data)
  {
    super.set(data);

    tryRebuildMatrix();

    data.imageName = imageName;
    data.colorCoefficients.setFrom(colorCoefficients);
    System.arraycopy(modelMatrix, 0, data.modelMatrix, 0, 16);
  }

  private void tryRebuildMatrix()
  {
    if (!needRebuildMatrix)
      return;

    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), position.getZ());
    Matrix.rotateM(modelMatrix, 0, angle, 0, 0, 1);
    Matrix.scaleM(modelMatrix, 0, width, height, 1);

    needRebuildMatrix = false;
  }

  public void setSize(float width, float height)
  {
    this.width = width;
    this.height = height;
    needRebuildMatrix = true;
  }

  public void setPosition(float x, float y)
  {
    position.setX(x);
    position.setY(y);
    needRebuildMatrix = true;
  }

  public void setPosition(Vector2 value)
  {
    position.setFrom(value);
    needRebuildMatrix = true;
  }

  public void setColorCoefficients(Vector3 coefficients)
  {
    colorCoefficients.setFrom(coefficients);
  }

  public void setImage(String value)
  {
    imageName = value;
  }

  public void setAngle(float value)
  {
    angle = value;
    needRebuildMatrix = true;
  }

  public void setZIndex(float value)
  {
    position.setY(value);
    needRebuildMatrix = true;
  }
}
