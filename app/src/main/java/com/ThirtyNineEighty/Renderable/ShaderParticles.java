package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;

public class ShaderParticles
  extends Shader
{
  public int attributePositionStartHandle;
  public int attributeVectorHandle;
  public int attributeColorHandle;
  public int attributeSpeedSizeHandle;

  public int uniformTimeHandle;
  public int uniformTextureHandle;

  @Override
  public void compile()
  {
    compile("Shaders/Particles.vert", "Shaders/Particles.frag");
  }

  @Override
  protected void getLocations()
  {
    attributePositionStartHandle = GLES20.glGetAttribLocation(programHandle, "a_positionStart");
    attributeVectorHandle        = GLES20.glGetAttribLocation(programHandle, "a_vector");
    attributeColorHandle         = GLES20.glGetAttribLocation(programHandle, "a_color");
    attributeSpeedSizeHandle     = GLES20.glGetAttribLocation(programHandle, "a_speedSize");

    uniformTimeHandle            = GLES20.glGetUniformLocation(programHandle, "u_time");
    uniformTextureHandle         = GLES20.glGetUniformLocation(programHandle, "u_texture");
  }
}
