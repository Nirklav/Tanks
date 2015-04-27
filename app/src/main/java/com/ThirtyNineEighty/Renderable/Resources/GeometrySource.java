package com.ThirtyNineEighty.Renderable.Resources;

import android.opengl.GLES20;
import android.opengl.GLException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class GeometrySource
  implements ISource<Geometry>
{
  protected final String name;
  protected final MeshMode mode;

  protected GeometrySource(String geometryName, MeshMode meshMode)
  {
    name = geometryName;
    mode = meshMode;
  }

  @Override
  public String getName() { return String.format("%s-%s", name, mode.name()); }

  @Override
  public Geometry load()
  {
    LoadResult result = buildGeometry();
    switch (mode)
    {
    case Dynamic:
      return new Geometry(result.buffer, result.trianglesCount);

    case Static:
      int handle = loadGeometry(result.buffer);
      return new Geometry(handle, result.trianglesCount);
    }

    throw new IllegalArgumentException("Invalid mesh mode");
  }

  @Override
  public void reload(Geometry geometry)
  {
    release(geometry);

    LoadResult result = buildGeometry();
    switch (mode)
    {
    case Dynamic:
      geometry.updateData(result.buffer, result.trianglesCount);
      return;

    case Static:
      int handle = loadGeometry(result.buffer);
      geometry.updateData(handle, result.trianglesCount);
      return;
    }

    throw new IllegalArgumentException("Invalid mesh mode");
  }

  @Override
  public void release(Geometry geometry)
  {
    switch (mode)
    {
    case Dynamic:
      geometry.updateData(null, 0);
      break;

    case Static:
      int handle = geometry.getHandle();
      if (GLES20.glIsBuffer(handle))
        GLES20.glDeleteBuffers(1, new int[] { handle }, 0);

      geometry.updateData(0, 0);
      break;
    }
  }

  protected abstract LoadResult buildGeometry();

  private static int loadGeometry(FloatBuffer buffer)
  {
    int error;
    int[] buffers = new int[1];

    GLES20.glGenBuffers(1, buffers, 0);
    if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
      throw new GLException(error, Integer.toString(error));

    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
    if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
      throw new GLException(error, Integer.toString(error));

    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GLES20.GL_STATIC_DRAW);
    if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
      throw new GLException(error, Integer.toString(error));

    return buffers[0];
  }

  protected static FloatBuffer loadGeometry(float[] bufferData)
  {
    return (FloatBuffer) ByteBuffer.allocateDirect(bufferData.length * 4)
                                   .order(ByteOrder.nativeOrder())
                                   .asFloatBuffer()
                                   .put(bufferData)
                                   .position(0);
  }

  protected static class LoadResult
  {
    public final FloatBuffer buffer;
    public final int trianglesCount;

    public LoadResult(FloatBuffer buffer, int trianglesCount)
    {
      this.buffer = buffer;
      this.trianglesCount = trianglesCount;
    }
  }
}
