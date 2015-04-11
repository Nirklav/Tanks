package com.ThirtyNineEighty.Renderable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLUtils;
import android.util.Log;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

public final class Renderable
{
  private static HashMap<String, TextureData> texturesCache = new HashMap<String, TextureData>();
  private static HashMap<GeometryKey, GeometryData> geometryCache = new HashMap<GeometryKey, GeometryData>();

  public static TextureData loadTexture(String name, boolean generateMipmap)
  {
    if (texturesCache.containsKey(name))
      return texturesCache.get(name);

    try
    {
      String fileName = getTextureFileName(name);
      InputStream stream = GameContext.getAppContext().getAssets().open(fileName);
      Bitmap bitmap = BitmapFactory.decodeStream(stream);
      stream.close();

      int type = GLUtils.getType(bitmap);
      int format = GLUtils.getInternalFormat(bitmap);
      int error;

      int[] textures = new int[1];

      GLES20.glGenTextures(1, textures, 0);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error);

      GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error);

      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error);

      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, format, bitmap, type, 0);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error);

      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

      if (generateMipmap)
      {
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
          throw new GLException(error, Integer.toString(error));
      }

      bitmap.recycle();

      TextureData texture = new TextureData(textures[0]);
      texturesCache.put(name, texture);
      return texture;
    }
    catch(Exception e)
    {
      Log.e("Error", e.getMessage());
      return null;
    }
  }

  public static GeometryData loadGeometry(String name, int numOfTriangles, float[] bufferData) { return loadGeometry(name, numOfTriangles, bufferData, MeshMode.Static); }
  public static GeometryData loadGeometry(String name, int numOfTriangles, float[] bufferData, MeshMode mode)
  {
    GeometryKey key = new GeometryKey(name, mode);
    if (geometryCache.containsKey(key))
      return geometryCache.get(key);

    FloatBuffer data = (FloatBuffer)ByteBuffer.allocateDirect(bufferData.length * 4)
                                              .order(ByteOrder.nativeOrder())
                                              .asFloatBuffer()
                                              .put(bufferData)
                                              .position(0);

    GeometryData geometry;
    switch (mode)
    {
    case Static:
      int handle = getBufferHandle(data);
      geometry = new GeometryData(handle, numOfTriangles);
      break;

    case Dynamic:
      geometry = new GeometryData(data, numOfTriangles);
      break;

    default:
      return null;
    }

    geometryCache.put(key, geometry);
    return geometry;
  }

  public static GeometryData loadGeometry(String name)
  {
    GeometryKey key = new GeometryKey(name, MeshMode.Static);
    if (geometryCache.containsKey(key))
      return geometryCache.get(key);

    try
    {
      String fileName = getModelFileName(name);
      InputStream stream = GameContext.getAppContext().getAssets().open(fileName);

      int size = stream.available();
      byte[] data = new byte[size];
      stream.read(data);
      stream.close();

      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(size - 4);
      dataBuffer.order(ByteOrder.nativeOrder());
      dataBuffer.put(data, 4, size - 4);
      dataBuffer.position(0);

      ByteBuffer numBuffer = ByteBuffer.allocateDirect(4);
      numBuffer.order(ByteOrder.nativeOrder());
      numBuffer.put(data, 0, 4);

      int numOfTriangles = numBuffer.getInt(0);
      int handle = getBufferHandle(dataBuffer.asFloatBuffer());

      GeometryData geometry = new GeometryData(handle, numOfTriangles);
      geometryCache.put(key, geometry);
      return geometry;
    }
    catch(IOException e)
    {
      Log.e("Error", e.getMessage());
      return null;
    }
  }

  @Deprecated
  public static void updateGeometry(GeometryData geometry, int numOfTriangles, float[] bufferData)
  {
    throw new UnsupportedOperationException("not yet implemented");
  }

  private static int getBufferHandle(FloatBuffer buffer)
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

  private static String getTextureFileName(String name)
  {
    return String.format("Textures/%s.png", name);
  }

  private static String getModelFileName(String name)
  {
    return String.format("Models/%s.raw", name);
  }

  public static void clearCache()
  {
    int counter = 0;
    int[] textures = new int[texturesCache.size()];
    for(TextureData texture : texturesCache.values())
      textures[counter++] = texture.handle;

    GLES20.glDeleteTextures(textures.length, textures, 0);
    texturesCache.clear();

    counter = 0;
    int[] buffers = new int[geometryCache.size()];
    for(GeometryData geometry : geometryCache.values())
      buffers[counter++] = geometry.handle;

    GLES20.glDeleteBuffers(buffers.length, buffers, 0);
    geometryCache.clear();
  }

  private static class GeometryKey
  {
    public final String name;
    public final MeshMode mode;

    public GeometryKey(String name, MeshMode mode)
    {
      this.name = name;
      this.mode = mode;
    }

    @Override
    public int hashCode() { return (name.hashCode() * 397) ^ mode.hashCode(); }

    @Override
    public boolean equals(Object o)
    {
      if (o == null)
        return false;

      if (this == o)
        return true;

      if (!(o instanceof GeometryKey))
        return false;

      GeometryKey other = (GeometryKey)o;
      return
        name.equals(other.name) &&
        mode.equals(other.mode);
    }
  }

  public static class GeometryData
  {
    private MeshMode mode;
    private int handle;
    private int numOfTriangles;
    private FloatBuffer data;

    public GeometryData(int handle, int numOfTriangles)
    {
      this.handle = handle;
      this.numOfTriangles = numOfTriangles;
      this.mode = MeshMode.Static;
      this.data = null;
    }

    public GeometryData(FloatBuffer data, int numOfTriangles)
    {
      this.handle = 0;
      this.numOfTriangles = numOfTriangles;
      this.mode = MeshMode.Dynamic;
      this.data = data;
    }

    public MeshMode getMode() { return mode; }

    public int getNumOfTriangles() { return numOfTriangles; }
    public int getHandle() { return handle; }

    public FloatBuffer getData()
    {
      if (mode != MeshMode.Dynamic)
        throw new IllegalStateException("not right mode");
      return data;
    }

    private void updateData(int numOfTriangles)
    {
      if (mode != MeshMode.Static)
        throw new IllegalStateException("not right mode");

      this.numOfTriangles = numOfTriangles;
    }

    private void updateData(FloatBuffer data, int numOfTriangles)
    {
      if (mode != MeshMode.Dynamic)
        throw new IllegalStateException("not right mode");

      this.data = data;
      this.numOfTriangles = numOfTriangles;
    }
  }

  public static class TextureData
  {
    public final int handle;

    public TextureData(int handle)
    {
      this.handle = handle;
    }
  }
}
