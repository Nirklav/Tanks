package com.ThirtyNineEighty.Renderable.Resources;

import android.opengl.GLES20;
import android.opengl.GLException;

import java.nio.FloatBuffer;

public abstract class GeometrySource
  implements ISource<Geometry>
{
  public abstract Geometry load();
  public abstract void reload(Geometry geometry);

  protected static int loadGeometry(FloatBuffer buffer)
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
}
