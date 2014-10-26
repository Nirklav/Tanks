package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;

public class Shader3D
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;
  public int attributeNormalHandle;
  public int uniformMatrixProjectionHandle;
  public int uniformMatrixHandle;
  public int uniformTextureHandle;
  public int uniformLightVectorHandle;

  @Override
  public void compile()
  {
    compile("Shaders/vertex3D.c", "Shaders/fragment3D.c");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    attributeNormalHandle         = GLES20.glGetAttribLocation(programHandle, "a_normal");
    uniformMatrixProjectionHandle = GLES20.glGetUniformLocation(programHandle, "u_ModelViewProjectionM");
    uniformMatrixHandle           = GLES20.glGetUniformLocation(programHandle, "u_ModelViewM");
    uniformLightVectorHandle      = GLES20.glGetUniformLocation(programHandle, "u_light");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
  }
}
