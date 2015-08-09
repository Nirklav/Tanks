package com.ThirtyNineEighty.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderParticles
  extends Shader
{
  public int attributePositionStartHandle;
  public int attributeDirectionVectorHandle;
  public int attributeColorHandle;

  public int uniformLifeTimeHandle;
  public int uniformMatrixHandle;

  @Override
  public void compile()
  {
    compile("Shaders/Particles.vert", "Shaders/Particles.frag");
  }

  @Override
  protected void getLocations()
  {
    attributePositionStartHandle   = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeDirectionVectorHandle = GLES20.glGetAttribLocation(programHandle, "a_directionVector");
    attributeColorHandle           = GLES20.glGetAttribLocation(programHandle, "a_color");

    uniformLifeTimeHandle              = GLES20.glGetUniformLocation(programHandle, "u_lifeTime");
    uniformMatrixHandle            = GLES20.glGetUniformLocation(programHandle, "u_matrix");
  }
}
