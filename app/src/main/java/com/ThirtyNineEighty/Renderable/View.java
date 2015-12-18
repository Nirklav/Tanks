package com.ThirtyNineEighty.Renderable;

import com.ThirtyNineEighty.System.EngineObject;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IBindable;
import com.ThirtyNineEighty.System.IView;

public abstract class View
  extends EngineObject
  implements IView
{
  private static final long serialVersionUID = 1L;

  private IBindable bindable;

  @Override
  public void setBindable(IBindable value)
  {
    bindable = value;
  }

  protected void unbind()
  {
    final IView view = this;
    GameContext.content.postEvent(new Runnable()
    {
      @Override
      public void run()
      {
        bindable.unbind(view);
      }
    });
  }
}
