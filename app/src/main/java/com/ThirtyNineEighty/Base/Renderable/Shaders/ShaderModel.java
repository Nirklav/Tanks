package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderModel
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;
  public int attributeNormalHandle;

  public int uniformMatrixProjectionHandle;
  public int uniformMatrixHandle;
  public int uniformNormalMatrix;
  public int uniformTextureHandle;
  public int uniformLightVectorHandle;
  public int uniformColorCoefficients;

  @Override
  public void compile()
  {
    compile("Shaders/Model.vert", "Shaders/Model.frag");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    attributeNormalHandle         = GLES20.glGetAttribLocation(programHandle, "a_normal");
    uniformMatrixProjectionHandle = GLES20.glGetUniformLocation(programHandle, "u_modelViewProjectionMatrix");
    uniformMatrixHandle           = GLES20.glGetUniformLocation(programHandle, "u_modelMatrix");
    uniformNormalMatrix           = GLES20.glGetUniformLocation(programHandle, "u_modelNormalMatrix");
    uniformLightVectorHandle      = GLES20.glGetUniformLocation(programHandle, "u_light");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformColorCoefficients      = GLES20.glGetUniformLocation(programHandle, "u_colorCoefficients");
  }
}
