package com.ThirtyNineEighty.System;

public abstract class InitializableSubprogram implements ISubprogram
{
  private boolean initialized;
  private long lifeTime;

  public InitializableSubprogram()
  {
    lifeTime = -1;
  }

  @Override
  public final void update()
  {
    if (!initialized)
    {
      onInitialize();
      initialized = true;
    }

    if (lifeTime >= 0)
    {
      if (lifeTime == 0)
      {
        onLifeTimeEnd();
        stopProgram();
        return;
      }

      lifeTime--;
    }

    onUpdate();
  }

  protected void onInitialize() { }
  protected abstract void onUpdate();
  protected void onLifeTimeEnd() { }
  protected void onUninitialize() { }

  protected void stopProgram()
  {
    IContent content = GameContext.getContent();
    content.unbindProgram(this);

    onUninitialize();
  }

  public InitializableSubprogram setLifeTime(long updatesCount)
  {
    lifeTime = updatesCount;
    return this;
  }
}
