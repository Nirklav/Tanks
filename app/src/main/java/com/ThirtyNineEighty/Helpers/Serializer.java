package com.ThirtyNineEighty.Helpers;

import com.ThirtyNineEighty.System.GameContext;

import java.io.InputStream;
import java.io.ObjectInputStream;

public final class Serializer
{
  @SuppressWarnings("unchecked")
  public static <T> T Deserialize(String fileName)
  {
    try
    {
      InputStream stream = GameContext.activity.getAssets().open(fileName);
      ObjectInputStream objectStream = new ObjectInputStream(stream);
      T result = (T) objectStream.readObject();
      objectStream.close();
      stream.close();

      return result;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
