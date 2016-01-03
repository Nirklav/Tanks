package com.ThirtyNineEighty.Base;

import java.io.Serializable;

public interface IEngineObject
  extends Serializable
{
  Long getId();

  boolean isInitialized();
  boolean isEnabled();

  void initialize();
  void uninitialize();

  void enable();
  void disable();
}
