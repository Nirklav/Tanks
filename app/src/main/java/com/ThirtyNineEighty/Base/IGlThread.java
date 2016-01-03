package com.ThirtyNineEighty.Base;

public interface IGlThread
{
  void postEvent(Runnable r);
  void sendEvent(Runnable r);
}
