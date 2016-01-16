package com.ThirtyNineEighty.Base.Resources.Sources;

import android.opengl.GLES20;
import android.opengl.GLException;

import com.ThirtyNineEighty.Base.Common.ResultRunnable;
import com.ThirtyNineEighty.Base.Renderable.Renderer;
import com.ThirtyNineEighty.Base.Resources.Entities.FrameBuffer;
import com.ThirtyNineEighty.Base.GameContext;

public class FrameBufferSource
  implements ISource<FrameBuffer>
{
  public static final int ShadowBuffer = 0;

  private String name;

  public FrameBufferSource(int number)
  {
    name = String.format("frameBuffer%d", number);
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public FrameBuffer load()
  {
    return build();
  }

  @Override
  public void reload(FrameBuffer frameBuffer)
  {
    FrameBuffer buffer = build();

    frameBuffer.setDepthTextureHandle(buffer.depthTextureHandle);
    frameBuffer.setRenderTextureHandle(buffer.renderTextureHandle);
    frameBuffer.setFrameBufferHandle(buffer.frameBufferHandle);
  }

  @Override
  public void release(FrameBuffer frameBuffer)
  {
    GLES20.glDeleteFramebuffers(1, new int[] { frameBuffer.frameBufferHandle }, 0);
    GLES20.glDeleteRenderbuffers(1, new int[]{ frameBuffer.renderTextureHandle }, 0);
    GLES20.glDeleteTextures(1, new int[]{ frameBuffer.depthTextureHandle }, 0);
  }

  private FrameBuffer build()
  {
    ResultRunnable<FrameBuffer> runnable = new ResultRunnable<FrameBuffer>()
    {
      @Override
      protected FrameBuffer onRun()
      {
        int width = (int) Renderer.getWidth();
        int height = (int) Renderer.getHeight();

        int[] renderTextureIds = new int[1];
        GLES20.glGenTextures(1, renderTextureIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderTextureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        int[] frameBufferIds = new int[1];
        GLES20.glGenFramebuffers(1, frameBufferIds, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferIds[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, renderTextureIds[0], 0);

        int[] depthTextureIds = new int[1];
        GLES20.glGenRenderbuffers(1, depthTextureIds, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthTextureIds[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthTextureIds[0]);

        // check FBO status
        int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE)
          throw new GLException(status, "GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");

        return new FrameBuffer(name, frameBufferIds[0], depthTextureIds[0], renderTextureIds[0]);
      }
    };

    GameContext.glThread.sendEvent(runnable);
    return runnable.getResult();
  }
}
