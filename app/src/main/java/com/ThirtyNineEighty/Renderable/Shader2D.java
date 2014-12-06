package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;

public class Shader2D
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;

  public int uniformModelViewMatrixHandle;
  public int uniformTextureMatrixHandle;
  public int uniformTextureHandle;

  @Override
  public void compile()
  {
    compile("Shaders/vertex2D.c", "Shaders/fragment2D.c");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    uniformModelViewMatrixHandle  = GLES20.glGetUniformLocation(programHandle, "u_modelViewMatrix");
    uniformTextureMatrixHandle    = GLES20.glGetUniformLocation(programHandle, "u_texMatrix");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
  }
}
