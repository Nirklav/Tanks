package com.ThirtyNineEighty.Game.Map.Factory;

import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;

public class Creator<T>
{
  private Class<T> type;
  private Constructor<T>[] constructors;

  @SuppressWarnings("unchecked")
  public Creator(Class<T> type)
  {
    this.type = type;
    this.constructors = (Constructor<T>[])type.getConstructors();
  }

  public T create(Object... allParams)
  {
    try
    {
      ArrayList<ConstructorContainer<T>> containers = new ArrayList<>();

      for (Constructor<T> ctor : constructors)
      {
        boolean isRightCtor = true;

        Class<?>[] parameterTypes = ctor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++)
        {
          for (Object parameter : allParams)
          {
            if (parameterTypes[i].isAssignableFrom(parameter.getClass()))
              params[i] = parameter;
          }

          if (params[i] == null)
          {
            isRightCtor = false;
            break;
          }
        }

        if (isRightCtor)
          containers.add(new ConstructorContainer<>(ctor, params));
      }

      if (containers.size() == 0)
        throw new InvalidParameterException(String.format("For type %s can't find ctor", type));

      return Collections.max(containers).Create();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private static class ConstructorContainer<T>
    implements Comparable<ConstructorContainer<T>>
  {
    private final Constructor<T> constructor;
    private final Object[] params;

    public ConstructorContainer(Constructor<T> constructor, Object[] params)
    {
      this.constructor = constructor;
      this.params = params;
    }

    public T Create() throws Exception
    {
      return constructor.newInstance(params);
    }

    @Override
    public int compareTo(@NonNull ConstructorContainer<T> another)
    {
      return params.length - another.params.length;
    }
  }
}
