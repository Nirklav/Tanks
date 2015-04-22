package com.ThirtyNineEighty.Renderable.Resources;

public class Texture
{
  private int handle;

  public Texture(int handle)
  {
    this.handle = handle;
  }

  public int getHandle() { return handle; }
  public void setHandle(int value) { handle = value; }
}
