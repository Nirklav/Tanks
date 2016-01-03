package com.ThirtyNineEighty.Base.Common;

import com.ThirtyNineEighty.Base.GameContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Serializer
{
  public static byte[] Serialize(Object object)
  {
    try
    {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(stream);
      objectStream.writeObject(object);
      byte[] result = stream.toByteArray();
      objectStream.close();
      stream.close();

      return result;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static <T> T Deserialize(String fileName)
  {
    try
    {
      return Deserialize(GameContext.context.getAssets().open(fileName));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static <T> T Deserialize(byte[] data)
  {
    return Deserialize(new ByteArrayInputStream(data));
  }

  @SuppressWarnings("unchecked")
  private static <T> T Deserialize(InputStream stream)
  {
    try
    {
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
