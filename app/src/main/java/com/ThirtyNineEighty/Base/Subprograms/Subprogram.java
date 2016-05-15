package com.ThirtyNineEighty.Base.Subprograms;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.EngineObject;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.IBindable;

import java.util.ArrayList;

public abstract class Subprogram
  extends EngineObject
  implements ISubprogram
{
  private static final long serialVersionUID = 1L;

  private ITaskAdder adder;
  private ArrayList<ITask> tasks;

  private IBindable bindable;
  private float delay;

  protected Subprogram()
  {
    tasks = new ArrayList<>();
  }

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
    if (!tasksCompleted())
      return;

    this.adder = adder;
    onPrepare();
    this.adder = null;
  }

  @Override
  public final void update()
  {
    if (!tasksCompleted())
      return;

    if (delay > 0)
    {
      delay -= 1000 * DeltaTime.get();
      return;
    }

    onUpdate();
  }

  private boolean tasksCompleted()
  {
    for (ITask task : tasks)
    {
      if (!task.isCompleted())
        return false;
    }

    tasks.clear();
    return true;
  }

  protected void onPrepare() {  }
  protected abstract void onUpdate();

  protected final void addTask(int priority, Runnable task)
  {
    if (adder == null)
      throw new IllegalStateException("tasks can be added only in onPrepare method");

    tasks.add(adder.add(priority, task));
  }

  protected void delay(float ms)
  {
    delay = ms;
  }
}
