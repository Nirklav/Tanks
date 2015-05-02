package com.ThirtyNineEighty.System;

public abstract class Subprogram
  implements ISubprogram
{
  private boolean enabled = true;

  @Override
  public void update()
  {
    if (!enabled)
      return;

    onUpdate();
  }

  @Override
  public void enable() { enabled = true; }

  @Override
  public void disable() { enabled = false; }

  protected abstract void onUpdate();
}
