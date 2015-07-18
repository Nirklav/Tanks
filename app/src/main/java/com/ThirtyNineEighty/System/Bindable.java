package com.ThirtyNineEighty.System;

import java.util.ArrayList;

public class Bindable
  implements IBindable
{
  private final ArrayList<ISubprogram> subprograms;
  private volatile boolean initialized;

  public Bindable()
  {
    subprograms = new ArrayList<>();
  }

  @Override
  public boolean isInitialized() { return initialized; }

  @Override
  public void initialize()
  {
    initialized = true;
    enable();
  }

  @Override
  public void uninitialize()
  {
    initialized = false;

    for (ISubprogram subprogram : getSubprogramsCopy())
      unbindProgram(subprogram);
  }

  @Override
  public void enable()
  {
    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.enable();
  }

  @Override
  public void disable()
  {
    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.disable();
  }

  @Override
  public void bindProgram(ISubprogram subprogram)
  {
    if (initialized)
      subprogram.enable();
    else
      subprogram.disable();

    GameContext.content.bindProgram(subprogram);
    subprogram.setBindable(this);

    synchronized (subprograms)
    {
      subprograms.add(subprogram);
    }
  }

  @Override
  public void unbindProgram(ISubprogram subprogram)
  {
    GameContext.content.unbindProgram(subprogram);
    subprogram.disable();
    subprogram.setBindable(null);

    synchronized (subprograms)
    {
      subprograms.remove(subprogram);
    }
  }

  private ArrayList<ISubprogram> getSubprogramsCopy()
  {
    synchronized (subprograms)
    {
      return new ArrayList<>(subprograms);
    }
  }
}
