package com.ThirtyNineEighty.Base.Resources.Sources;

import android.opengl.GLES20;
import android.opengl.GLException;

import com.ThirtyNineEighty.Base.Common.ResultRunnable;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.GameContext;

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
    name = String.format("%s-%s", geometryName, meshMode.name());
    mode = meshMode;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Geometry load()
  {
    LoadResult result = buildGeometry();
    switch (mode)
    {
    case Dynamic:
      return new Geometry(name, result.buffer, result.pointsCount, result.position, result.angles);

    case Static:
      int handle = loadGeometry(result.buffer);
      return new Geometry(name, handle, result.pointsCount, result.position, result.angles);
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
      geometry.updateData(result.buffer, result.pointsCount, result.position, result.angles);
      return;

    case Static:
      int handle = loadGeometry(result.buffer);
      geometry.updateData(handle, result.pointsCount, result.position, result.angles);
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
      geometry.updateData(null, 0, null, null);
      break;

    case Static:
      int handle = geometry.getHandle();
      if (GLES20.glIsBuffer(handle))
        GLES20.glDeleteBuffers(1, new int[] { handle }, 0);

      geometry.updateData(0, 0, null, null);
      break;
    }
  }

  protected abstract LoadResult buildGeometry();

  private static int loadGeometry(final FloatBuffer buffer)
  {
    ResultRunnable<Integer> runnable = new ResultRunnable<Integer>()
    {
      @Override
      protected Integer onRun()
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
    };

    GameContext.glThread.sendEvent(runnable);
    return runnable.getResult();
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
    public final int pointsCount;
    public final Vector3 position;
    public final Vector3 angles;

    public LoadResult(FloatBuffer buffer, int pointsCount, Vector3 position, Vector3 angles)
    {
      this.buffer = buffer;
      this.pointsCount = pointsCount;
      this.position = position;
      this.angles = angles;
    }
  }
}
