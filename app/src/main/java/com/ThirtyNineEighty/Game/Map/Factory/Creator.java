package com.ThirtyNineEighty.Game.Map.Factory;

import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;

public class Creator<T>
{
  public Class<T> type;
  public Class<?>[] parameterTypes;

  public Creator(Class<T> type, Class<?>... parameterTypes)
  {
    this.type = type;

    if (parameterTypes != null)
      this.parameterTypes = parameterTypes;
    else
      this.parameterTypes = new Class<?>[0];
  }

  public T create(Object... allParams)
  {
    try
    {
      Object[] params = new Object[parameterTypes.length];
      for (int i = 0; i < parameterTypes.length; i++)
      {
        Class<?> parameterType = parameterTypes[i];
        for (Object parameter : allParams)
        {
          if (parameterType.isAssignableFrom(parameter.getClass()))
            params[i] = parameter;
        }
      }

      Constructor<T> ctor = type.getConstructor(parameterTypes);
      if (ctor != null)
        return ctor.newInstance(params);

      throw new InvalidParameterException(String.format("For type %s can't find ctor", type));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
