package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Game.Subprograms.ISubprogram;
import com.ThirtyNineEighty.Renderable.IRenderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Bindable
  extends EngineObject
  implements IBindable
{
  private static final long serialVersionUID = 1L;

  private final HashMap<Long, ISubprogram> subprograms = new HashMap<>();
  private final HashMap<Long, IRenderable> renderables = new HashMap<>();

  @Override
  public void initialize()
  {
    super.initialize();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.initialize();

    for (IRenderable renderable : getRenderablesCopy())
      renderable.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.uninitialize();

    for (IRenderable renderable : getRenderablesCopy())
      renderable.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    for (IRenderable renderable : getRenderablesCopy())
      renderable.enable();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    for (IRenderable renderable : getRenderablesCopy())
      renderable.disable();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.disable();
  }

  @Override
  public List<IRenderable> getRenderables() { return getRenderablesCopy(); }

  @Override
  public List<ISubprogram> getSubprograms() { return getSubprogramsCopy(); }

  @Override
  public void bind(ISubprogram subprogram)
  {
    if (subprogram.isInitialized())
      throw new IllegalArgumentException("Subprogram should be not initialized");

    if (subprogram.isEnabled())
      throw new IllegalArgumentException("Subprogram should be disabled");

    subprogram.setBindable(this);

    if (isInitialized())
      subprogram.initialize();

    if (isEnabled())
      subprogram.enable();

    synchronized (subprograms)
    {
      Long id = subprogram.getId();
      if (subprograms.containsKey(id))
        throw new IllegalStateException("Subprogram with this id already added");

      subprograms.put(id, subprogram);
    }
  }

  @Override
  public void unbind(ISubprogram subprogram)
  {
    subprogram.setBindable(null);

    if (subprogram.isEnabled())
      subprogram.disable();

    if (subprogram.isInitialized())
      subprogram.uninitialize();

    synchronized (subprograms)
    {
      subprograms.remove(subprogram.getId());
    }
  }

  @Override
  public void bind(IRenderable renderable)
  {
    if (renderable.isInitialized())
      throw new IllegalArgumentException("Subprogram should be not initialized");

    if (renderable.isEnabled())
      throw new IllegalArgumentException("Subprogram should be disabled");

    if (isInitialized())
      renderable.initialize();

    if (isEnabled())
      renderable.enable();

    renderable.setBindable(this);

    synchronized (renderables)
    {
      Long id = renderable.getId();
      if (renderables.containsKey(id))
        throw new IllegalStateException("Renderable with this id already added");

      renderables.put(id, renderable);
    }
  }

  @Override
  public void unbind(IRenderable renderable)
  {
    renderable.setBindable(null);

    if (renderable.isEnabled())
      renderable.disable();

    if (renderable.isInitialized())
      renderable.uninitialize();

    synchronized (renderables)
    {
      renderables.remove(renderable.getId());
    }
  }

  protected ArrayList<ISubprogram> getSubprogramsCopy()
  {
    synchronized (subprograms)
    {
      return new ArrayList<>(subprograms.values());
    }
  }

  protected ArrayList<IRenderable> getRenderablesCopy()
  {
    synchronized (renderables)
    {
      return new ArrayList<>(renderables.values());
    }
  }
}
