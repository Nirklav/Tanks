package com.ThirtyNineEighty.System;

import android.content.Context;

public class ActivityContext
{
  private static Context context;

  public static Context getContext()
  {
    return context;
  }

  public static void setContext(Context value)
  {
    context = value;
  }
}
