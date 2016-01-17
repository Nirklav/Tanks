package com.ThirtyNineEighty.Base.Renderable.Shaders;

import android.opengl.GLES20;

public class ShaderPolyLine
  extends Shader
{
  public int attributePositionHandle;

  public int uniformColorHandle;
  public int uniformMatrixProjectionHandle;

  @Override
  public void compile()
  {
    compile("Shaders/PolyLine.vert", "Shaders/PolyLine.frag");
  }

  @Override
  protected void getLocations()
  {
    attributePositionHandle =       GLES20.glGetAttribLocation(programHandle, "a_position");
    uniformMatrixProjectionHandle = GLES20.glGetUniformLocation(programHandle, "u_modelViewProjectionMatrix");
    uniformColorHandle =            GLES20.glGetUniformLocation(programHandle, "u_color");
  }
}
