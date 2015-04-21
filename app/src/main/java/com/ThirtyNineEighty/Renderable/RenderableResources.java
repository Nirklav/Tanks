package com.ThirtyNineEighty.Renderable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLUtils;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

public final class RenderableResources
{
  private HashMap<String, Texture> texturesCache = new HashMap<String, Texture>();
  private HashMap<String, Image> imagesCache = new HashMap<String, Image>();
  private HashMap<GeometryKey, Geometry> geometryCache = new HashMap<GeometryKey, Geometry>();

  public Texture getTexture(String name, boolean generateMipmap)
  {
    if (texturesCache.containsKey(name))
      return texturesCache.get(name);

    int handle = loadTexture(name, generateMipmap);
    Texture texture = new Texture(handle, generateMipmap);
    texturesCache.put(name, texture);
    return texture;
  }

  private static int loadTexture(String name, boolean generateMipmap)
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
      return textures[0];
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public Geometry getGeometry(String name, int numOfTriangles, float[] bufferData) { return getGeometry(name, numOfTriangles, bufferData, MeshMode.Static); }
  public Geometry getGeometry(String name, int numOfTriangles, float[] bufferData, MeshMode mode)
  {
    GeometryKey key = new GeometryKey(name, mode);
    if (geometryCache.containsKey(key))
      return geometryCache.get(key);

    FloatBuffer data = (FloatBuffer)ByteBuffer.allocateDirect(bufferData.length * 4)
                                              .order(ByteOrder.nativeOrder())
                                              .asFloatBuffer()
                                              .put(bufferData)
                                              .position(0);

    Geometry geometry;
    switch (mode)
    {
    case Static:
      int handle = loadGeometry(data);
      geometry = new Geometry(handle, numOfTriangles);
      break;

    case Dynamic:
      geometry = new Geometry(data, numOfTriangles);
      break;

    default:
      return null;
    }

    geometryCache.put(key, geometry);
    return geometry;
  }

  public Geometry getGeometry(String name)
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
      int handle = loadGeometry(dataBuffer.asFloatBuffer());

      Geometry geometry = new Geometry(handle, numOfTriangles);
      geometryCache.put(key, geometry);
      return geometry;
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

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

  public void reloadCache()
  {
    reloadTextures();
    reloadGeometry();
  }

  private void reloadTextures()
  {
    for (String name : texturesCache.keySet())
    {
      Texture texture = texturesCache.get(name);
      GLES20.glDeleteTextures(1, new int[] { texture.getHandle() }, 0);

      int handle = loadTexture(name, texture.isMipmapsGenerated());
      texture.setHandle(handle);
    }
  }

  private void reloadGeometry()
  {
    for (GeometryKey key : geometryCache.keySet())
    {
      if (key.mode == MeshMode.Dynamic)
        continue;

      Geometry geometry = geometryCache.get(key);
      GLES20.glDeleteBuffers(1, new int[] { geometry.getHandle() }, 0);


    }
  }

  public void clearCache()
  {
    clearTexturesCache();
    clearGeometryCache();
  }

  private void clearTexturesCache()
  {
    int counter = 0;
    int[] textures = new int[texturesCache.size()];
    for(Texture texture : texturesCache.values())
      textures[counter++] = texture.getHandle();

    GLES20.glDeleteTextures(textures.length, textures, 0);
    texturesCache.clear();
  }

  private void clearGeometryCache()
  {
    int counter = 0;
    int[] buffers = new int[geometryCache.size()];
    for(Geometry geometry : geometryCache.values())
      buffers[counter++] = geometry.getHandle();

    GLES20.glDeleteBuffers(buffers.length, buffers, 0);
    geometryCache.clear();
  }

  private static String getTextureFileName(String name)
  {
    return String.format("Textures/%s.png", name);
  }

  private static String getModelFileName(String name)
  {
    return String.format("Models/%s.raw", name);
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
}
