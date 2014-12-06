package com.ThirtyNineEighty.Renderable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.ThirtyNineEighty.System.GameContext;

import java.io.InputStream;
import java.util.HashMap;

public final class Renderable
{
  private static HashMap<String, Integer> cache = new HashMap<String, Integer>();

  public static int loadTexture(String fileName, boolean generateMipmap)
  {
    if (cache.containsKey(fileName))
      return cache.get(fileName);

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

      cache.put(fileName, textures[0]);

      return textures[0];
    }
    catch(Exception e)
    {
      Log.e("Error", e.getMessage());

      return 0;
    }
  }

  public static void clearCache()
  {
    cache.clear();
  }
}
