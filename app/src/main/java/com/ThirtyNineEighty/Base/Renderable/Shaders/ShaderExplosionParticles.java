package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderExplosionParticles
  extends Shader
{
  public int attributeDirectionVectorHandle;

  public int uniformLifeTimeHandle;
  public int uniformMatrixHandle;
  public int uniformTextureHandle;
  public int uniformPointSize;
  public int uniformExplosionSize;
  public int uniformStartColor;
  public int uniformEndColor;

  @Override
  public void compile()
  {
    compile("Shaders/ExplosionParticles.vert", "Shaders/ExplosionParticles.frag");
  }

  @Override
  protected void getLocations()
  {
    attributeDirectionVectorHandle = GLES20.glGetAttribLocation(programHandle, "a_directionVector");

    uniformLifeTimeHandle          = GLES20.glGetUniformLocation(programHandle, "u_lifeTime");
    uniformMatrixHandle            = GLES20.glGetUniformLocation(programHandle, "u_matrix");
    uniformTextureHandle           = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformPointSize               = GLES20.glGetUniformLocation(programHandle, "u_pointSize");
    uniformExplosionSize           = GLES20.glGetUniformLocation(programHandle, "u_explosionSize");
    uniformStartColor              = GLES20.glGetUniformLocation(programHandle, "u_startColor");
    uniformEndColor                = GLES20.glGetUniformLocation(programHandle, "u_endColor");
  }
}
