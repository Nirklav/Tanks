package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderParticles
  extends Shader
{
  public int attributeDirectionVectorHandle;
  public int attributeStartTimeHandle;

  public int uniformLifeTimeHandle;
  public int uniformMatrixHandle;
  public int uniformTextureHandle;
  public int uniformPointSize;
  public int uniformStartColor;
  public int uniformEndColor;

  @Override
  public void compile()
  {
    compile("Shaders/Particles.vert", "Shaders/Particles.frag");
  }

  @Override
  protected void getLocations()
  {
    attributeDirectionVectorHandle = GLES20.glGetAttribLocation(programHandle, "a_directionVector");
    attributeStartTimeHandle       = GLES20.glGetAttribLocation(programHandle, "a_startTime");

    uniformLifeTimeHandle          = GLES20.glGetUniformLocation(programHandle, "u_lifeTime");
    uniformMatrixHandle            = GLES20.glGetUniformLocation(programHandle, "u_matrix");
    uniformTextureHandle           = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformPointSize               = GLES20.glGetUniformLocation(programHandle, "u_pointSize");
    uniformStartColor              = GLES20.glGetUniformLocation(programHandle, "u_startColor");
    uniformEndColor                = GLES20.glGetUniformLocation(programHandle, "u_endColor");
  }
}
