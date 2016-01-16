package com.ThirtyNineEighty.Base.Resources.Entities;

public class FrameBuffer
  extends Resource
{
  public int frameBufferHandle;
  public int depthTextureHandle;
  public int renderTextureHandle;

  public FrameBuffer(String name, int frameBufferHandle, int depthTextureHandle, int renderTextureHandle)
  {
    super(name);

    this.frameBufferHandle = frameBufferHandle;
    this.depthTextureHandle = depthTextureHandle;
    this.renderTextureHandle = renderTextureHandle;
  }

  public int getFrameBufferHandle() { return frameBufferHandle; }
  public void setFrameBufferHandle(int value) { frameBufferHandle = value; }

  public int getDepthTextureHandle() { return depthTextureHandle; }
  public void setDepthTextureHandle(int value) { depthTextureHandle = value; }

  public int getRenderTextureHandle() { return renderTextureHandle; }
  public void setRenderTextureHandle(int value) { renderTextureHandle = value; }
}
