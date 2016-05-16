package com.ThirtyNineEighty.Base.Subprograms;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.EngineObject;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.IBindable;

public abstract class Subprogram
  extends EngineObject
  implements ISubprogram
{
  private static final long serialVersionUID = 1L;

  private IBindable bindable;
  private float delay;

  @Override
  public void initialize()
  {
    super.initialize();

    GameContext.content.bindProgram(this);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    GameContext.content.unbindProgram(this);
  }

  @Override
  public void setBindable(IBindable value)
  {
    bindable = value;
  }

  protected void unbind()
  {
    bindable.unbind(this);
  }

  @Override
  public final void prepare(ITaskAdder adder)
  {
    onPrepare(adder);
  }

  @Override
  public final void update()
  {
    if (delay > 0)
    {
      delay -= 1000 * DeltaTime.get();
      return;
    }

    onUpdate();
  }

  protected void onPrepare(ITaskAdder adder) {  }
  protected abstract void onUpdate();

  protected void delay(float ms)
  {
    delay = ms;
  }
}
