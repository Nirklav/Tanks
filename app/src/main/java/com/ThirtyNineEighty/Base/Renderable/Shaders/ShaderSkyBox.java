package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderSkyBox
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;

  public int uniformMatrixProjectionHandle;
  public int uniformTextureHandle;

  @Override
  public void compile()
  {
    compile("Shaders/skyBox.vert", "Shaders/skyBox.frag");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    uniformMatrixProjectionHandle = GLES20.glGetUniformLocation(programHandle, "u_modelViewProjectionMatrix");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
  }
}
