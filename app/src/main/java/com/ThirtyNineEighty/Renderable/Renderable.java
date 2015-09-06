package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.System.EngineObject;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.IRenderable;

public abstract class Renderable
  extends EngineObject
  implements IRenderable
{
  private IBindable bindable;
  private boolean visible = true;

  @Override
  public boolean isVisible() { return visible; }

  @Override
  public void setVisible(boolean value) { visible = value; }

  @Override
  public void initialize()
  {
    super.initialize();

    GameContext.renderer.add(this);
  }

  @Override
  public void uninitialize()
  {
    GameContext.renderer.remove(this);

    super.uninitialize();
  }

  @Override
  public void setBindable(IBindable value)
  {
    bindable = value;
  }

  protected void unbind()
  {
    final IRenderable renderable = this;
    GameContext.content.postEvent(new Runnable()
    {
      @Override
      public void run()
      {
        bindable.unbind(renderable);
      }
    });
  }
}
