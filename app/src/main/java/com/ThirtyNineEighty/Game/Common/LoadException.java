package com.ThirtyNineEighty.Game.Common;

public class LoadException
  extends Exception
{
  public LoadException(String message)
  {
    super(message);
  }

  public LoadException(String message, Throwable throwable)
  {
    super(message, throwable);
  }
}
