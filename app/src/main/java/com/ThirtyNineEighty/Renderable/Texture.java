package com.ThirtyNineEighty.Renderable;

public class Texture
{
  private int handle;
  private boolean mipmaps;

  public Texture(int handle, boolean mipmaps)
  {
    this.handle = handle;
    this.mipmaps = mipmaps;
  }

  public int getHandle() { return handle; }
  public void setHandle(int value) { handle = value; }

  public boolean isMipmapsGenerated() { return mipmaps; }
}
