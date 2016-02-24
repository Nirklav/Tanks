package com.ThirtyNineEighty.Base.Renderable;

import com.ThirtyNineEighty.Base.EngineObject;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.IBindable;

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
        if (bindable != null)
          bindable.unbind(view);
      }
    });
  }
}
