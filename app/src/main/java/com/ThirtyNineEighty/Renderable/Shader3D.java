package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;

public class Shader3D extends Shader
{
  public int AttributePositionHandle;
  public int AttributeTexCoordHandle;
  public int AttributeNormalHandle;
  public int UniformMatrixProjectionHandle;
  public int UniformMatrixHandle;
  public int UniformTextureHandle;
  public int UniformLightVectorHandle;

  @Override
  public void Compile()
  {
    Compile("Shaders/vertex.c", "Shaders/fragment.c");
  }

  @Override
  protected void GetLocations()
  {
    //get shaders handles
    AttributePositionHandle       = GLES20.glGetAttribLocation(shaderProgramHandle, "a_position");
    AttributeTexCoordHandle       = GLES20.glGetAttribLocation(shaderProgramHandle, "a_texcoord");
    AttributeNormalHandle         = GLES20.glGetAttribLocation(shaderProgramHandle, "a_normal");
    UniformMatrixProjectionHandle = GLES20.glGetUniformLocation(shaderProgramHandle, "u_ModelViewProjectionM");
    UniformMatrixHandle           = GLES20.glGetUniformLocation(shaderProgramHandle, "u_ModelViewM");
    UniformLightVectorHandle      = GLES20.glGetUniformLocation(shaderProgramHandle, "u_light");
    UniformTextureHandle          = GLES20.glGetUniformLocation(shaderProgramHandle, "u_texture");
  }
}
