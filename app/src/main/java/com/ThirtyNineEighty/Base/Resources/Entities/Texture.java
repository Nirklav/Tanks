package com.ThirtyNineEighty.Base.Resources.Entities;

import android.opengl.GLES20;

import com.ThirtyNineEighty.Base.GameContext;

public class Texture
  extends Resource
{
  private int handle;

  public Texture(String name, int handle)
  {
    super(name);

    this.handle = handle;
  }

  public int getHandle() { return handle; }
  public void setHandle(int value) { handle = value; }

  public void validate()
  {
    if (!GameContext.debuggable)
      return;

    if (!GLES20.glIsTexture(handle))
      throw new IllegalStateException("Texture handle deprecated");
  }
}
