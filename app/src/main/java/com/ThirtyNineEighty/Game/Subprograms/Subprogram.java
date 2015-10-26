package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.System.EngineObject;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.State;

public abstract class Subprogram
  extends EngineObject
  implements ISubprogram
{
  private IBindable bindable;
  private float delay;

  public Subprogram(SubprogramState state)
  {
    super(state.name);
    delay = state.delay;
  }

  public Subprogram(String name)
  {
    super(name);
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
  public final void update()
  {
    if (delay > 0)
    {
      delay -= 1000 * GameContext.getDelta();
      return;
    }

    onUpdate();
  }

  protected abstract void onUpdate();

  protected void delay(float ms)
  {
    delay = ms;
  }

  @Override
  public State getState()
  {
    SubprogramState state = (SubprogramState) createState();
    state.name = getName();
    state.delay = delay;
    return state;
  }

  protected State createState()
  {
    return new SubprogramState();
  }

  protected static class SubprogramState
    extends State
  {
    private static final long serialVersionUID = 1L;

    private String name;
    private float delay;
  }
}
