package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderExplosionParticles
  extends Shader
{
  public int attributePositionStartHandle;
  public int attributeDirectionVectorHandle;
  public int attributeColorHandle;

  public int uniformLifeTimeHandle;
  public int uniformMatrixHandle;
  public int uniformTextureHandle;
  public int uniformPointSize;

  @Override
  public void compile()
  {
    compile("Shaders/ExplosionParticles.vert", "Shaders/ExplosionParticles.frag");
  }

  @Override
  protected void getLocations()
  {
    attributePositionStartHandle   = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeDirectionVectorHandle = GLES20.glGetAttribLocation(programHandle, "a_directionVector");
    attributeColorHandle           = GLES20.glGetAttribLocation(programHandle, "a_color");

    uniformLifeTimeHandle          = GLES20.glGetUniformLocation(programHandle, "u_lifeTime");
    uniformMatrixHandle            = GLES20.glGetUniformLocation(programHandle, "u_matrix");
    uniformTextureHandle           = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformPointSize               = GLES20.glGetUniformLocation(programHandle, "u_pointSize");
  }
}
