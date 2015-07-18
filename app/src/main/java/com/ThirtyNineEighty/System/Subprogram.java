package com.ThirtyNineEighty.System;

public abstract class Subprogram
  implements ISubprogram
{
  private float delay;
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

    if (delay > 0)
    {
      delay -= 1000 * GameContext.getDelta();
      return;
    }

    onUpdate();
  }

  protected abstract void onUpdate();

  @Override
  public void enable()
  {
    enabled = true;
  }

  @Override
  public void disable()
  {
    enabled = false;
  }

  protected void delay(float millis)
  {
    delay = millis;
  }

  protected void unbind()
  {
    if (bindable != null)
      bindable.unbindProgram(this);
    else
      GameContext.content.unbindProgram(this);
  }
}
