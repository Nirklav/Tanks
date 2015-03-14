package com.ThirtyNineEighty.Renderable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public final class Renderable
{
  private static HashMap<String, TextureData> texturesCache = new HashMap<String, TextureData>();
  private static HashMap<String, GeometryData> geometryCache = new HashMap<String, GeometryData>();

  public static TextureData loadTexture(String fileName, boolean generateMipmap)
  {
    if (texturesCache.containsKey(fileName))
      return texturesCache.get(fileName);

    try
    {
      InputStream stream = GameContext.getAppContext().getAssets().open(fileName);
      Bitmap bitmap = BitmapFactory.decodeStream(stream);
      stream.close();

      int type = GLUtils.getType(bitmap);
      int format = GLUtils.getInternalFormat(bitmap);

      int[] textures = new int[1];
      GLES20.glGenTextures(1, textures, 0);
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, format, bitmap, type, 0);

      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

      if (generateMipmap)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

      bitmap.recycle();

      int error;
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        Log.e("Error", Integer.toString(error));

      TextureData texture = new TextureData(textures[0]);
      texturesCache.put(fileName, texture);
      return texture;
    }
    catch(Exception e)
    {
      Log.e("Error", e.getMessage());
      return null;
    }
  }

  public static GeometryData load2DGeometry(String name, float[] bufferData)
  {
    if (geometryCache.containsKey(name))
      return geometryCache.get(name);

    Buffer data = ByteBuffer.allocateDirect(bufferData.length * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                            .put(bufferData)
                            .position(0);

    int[] buffers = new int[1];
    GLES20.glGenBuffers(1, buffers, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * 4, data, GLES20.GL_STATIC_DRAW);

    GeometryData geometry = new GeometryData(buffers[0], bufferData.length / 12);
    geometryCache.put(name, geometry);
    return geometry;
  }

  public static GeometryData load3DGeometry(String fileName)
  {
    if (geometryCache.containsKey(fileName))
      return geometryCache.get(fileName);

    try
    {
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

      int[] buffers = new int[1];
      GLES20.glGenBuffers(1, buffers, 0);
      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
      GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, dataBuffer.capacity(), dataBuffer, GLES20.GL_STATIC_DRAW);

      int error;
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        Log.e("Error", Integer.toString(error));

      GeometryData geometry = new GeometryData(buffers[0], numOfTriangles);
      geometryCache.put(fileName, geometry);
      return geometry;
    }
    catch(IOException e)
    {
      Log.e("Error", e.getMessage());
      return null;
    }
  }

  public static void clearCache()
  {
    for(TextureData texture : texturesCache.values())
      GLES20.glDeleteTextures(1, new int[] { texture.handle }, 0);

    texturesCache.clear();

    for(GeometryData geometry : geometryCache.values())
      GLES20.glDeleteBuffers(1, new int[] { geometry.handle }, 0);

    geometryCache.clear();
  }

  public static class GeometryData
  {
    public final int handle;
    public final int numOfTriangles;

    public GeometryData(int handle, int numOfTriangles)
    {
      this.handle = handle;
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
