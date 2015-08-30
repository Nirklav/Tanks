package com.ThirtyNineEighty.System;

public interface IEngineObject
{
  String getName();

  boolean isInitialized();
  boolean isEnabled();

  void initialize();
  void uninitialize();

  void enable();
  void disable();
}
