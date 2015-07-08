package com.ThirtyNineEighty.System;

public abstract class Subprogram
  implements ISubprogram
{
  private boolean enabled = true;
  private IBindable bindable;

  @Override
  public void setBindable(IBindable value)
  {
    bindable = value;
  }

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

  protected void unbind()
  {
    if (bindable != null)
      bindable.unbindProgram(this);
    else
      GameContext.content.unbindProgram(this);
  }
}
