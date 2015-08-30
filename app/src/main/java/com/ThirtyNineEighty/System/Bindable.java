package com.ThirtyNineEighty.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Bindable
  extends EngineObject
  implements IBindable
{
  private final HashMap<String, ISubprogram> subprograms = new HashMap<>();
  private final HashMap<String, IRenderable> renderables = new HashMap<>();

  protected Bindable()
  {

  }

  protected Bindable(String name)
  {
    super(name);
  }

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
      String name = subprogram.getName();

      if (subprograms.containsKey(name))
      {
        if (isInitialized())
          throw new IllegalStateException("Subprogram with this name already added");
        return;
      }

      subprograms.put(name, subprogram);
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
      subprograms.remove(subprogram.getName());
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
      String name = renderable.getName();

      if (renderables.containsKey(name))
      {
        if (isInitialized())
          throw new IllegalStateException("Renderable with this name already added");
        return;
      }

      renderables.put(name, renderable);
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
      renderables.remove(renderable.getName());
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
