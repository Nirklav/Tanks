package com.ThirtyNineEighty.Renderable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLUtils;
import android.util.Log;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;
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

  public static TextureData getTexture(String name)
  {
    if (texturesCache.containsKey(name))
      return texturesCache.get(name);

    return null;
  }

  public static GeometryData getGeometry(String name)
  {
    if (geometryCache.containsKey(name))
      return geometryCache.get(name);

    return null;
  }

  //TODO: Move all resource loading on GLThread or leave this :(
  public static void loadResources()
  {
    loadTexture("tank", true);
    loadTexture("land", true);
    loadTexture("bullet", true);
    loadTexture("button", false);

    load2DGeometry("GLSpriteMesh", GLSprite.bufferData); //TODO: the saddest place
    load3DGeometry("tank");
    load3DGeometry("land");
    load3DGeometry("bullet");
  }

  private static TextureData loadTexture(String name, boolean generateMipmap)
  {
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

  private static GeometryData load2DGeometry(String name, float[] bufferData)
  {
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

  private static GeometryData load3DGeometry(String name)
  {
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
      int error;
      int[] buffers = new int[1];

      GLES20.glGenBuffers(1, buffers, 0);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error, Integer.toString(error));

      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error, Integer.toString(error));

      GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, dataBuffer.capacity(), dataBuffer, GLES20.GL_STATIC_DRAW);
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        throw new GLException(error, Integer.toString(error));

      GeometryData geometry = new GeometryData(buffers[0], numOfTriangles);
      geometryCache.put(name, geometry);
      return geometry;
    }
    catch(IOException e)
    {
      Log.e("Error", e.getMessage());
      return null;
    }
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
