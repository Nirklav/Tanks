package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderLabel
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;

  public int uniformModelViewMatrixHandle;
  public int uniformTextureHandle;
  public int uniformColorCoefficients;

  @Override
  public void compile()
  {
    compile("Shaders/Label.vert", "Shaders/Label.frag");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    uniformModelViewMatrixHandle  = GLES20.glGetUniformLocation(programHandle, "u_modelViewMatrix");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformColorCoefficients      = GLES20.glGetUniformLocation(programHandle, "u_colorCoefficients");
  }
}