package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Renderable.IRenderable;

import java.util.ArrayList;

public class Bindable
  implements IBindable
{
  protected final ArrayList<ISubprogram> subprograms = new ArrayList<>();
  protected final ArrayList<IRenderable> renderables = new ArrayList<>();

  private volatile boolean initialized;

  @Override
  public boolean isInitialized() { return initialized; }

  @Override
  public void initialize()
  {
    initialized = true;
    enable();

    synchronized (renderables)
    {
      for (IRenderable renderable : renderables)
        GameContext.renderer.add(renderable);
    }
  }

  @Override
  public void uninitialize()
  {
    initialized = false;

    for (ISubprogram subprogram : getSubprogramsCopy())
      unbind(subprogram);

    for (IRenderable renderable : getRenderablesCopy())
      unbind(renderable);
  }

  @Override
  public void enable()
  {
    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.enable();
  }

  @Override
  public void disable()
  {
    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.disable();
  }

  @Override
  public void bind(ISubprogram subprogram)
  {
    if (initialized)
      subprogram.enable();
    else
      subprogram.disable();

    GameContext.content.bindProgram(subprogram);
    subprogram.setBindable(this);

    synchronized (subprograms)
    {
      subprograms.add(subprogram);
    }
  }

  @Override
  public void unbind(ISubprogram subprogram)
  {
    GameContext.content.unbindProgram(subprogram);
    subprogram.disable();
    subprogram.setBindable(null);

    synchronized (subprograms)
    {
      subprograms.remove(subprogram);
    }
  }

  @Override
  public void bind(IRenderable renderable)
  {
    if (isInitialized())
      GameContext.renderer.add(renderable);

    synchronized (renderables)
    {
      renderables.add(renderable);
    }
  }

  @Override
  public void unbind(IRenderable renderable)
  {
    GameContext.renderer.remove(renderable);

    synchronized (renderables)
    {
      renderables.remove(renderable);
    }
  }

  private ArrayList<ISubprogram> getSubprogramsCopy()
  {
    synchronized (subprograms)
    {
      return new ArrayList<>(subprograms);
    }
  }

  private ArrayList<IRenderable> getRenderablesCopy()
  {
    synchronized (renderables)
    {
      return new ArrayList<>(renderables);
    }
  }
}
