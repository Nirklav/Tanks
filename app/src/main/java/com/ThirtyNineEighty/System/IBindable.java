package com.ThirtyNineEighty.System;

public interface IBindable
{
  boolean isInitialized();

  void initialize(Object args);
  void uninitialize();

  void enable();
  void disable();

  void bindProgram(ISubprogram subprogram);
  void unbindProgram(ISubprogram subprogram);
}
