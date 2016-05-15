package com.ThirtyNineEighty.Base;

import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.IView;
import com.ThirtyNineEighty.Base.Subprograms.ISubprogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Bindable
  extends EngineObject
  implements IBindable
{
  private static final long serialVersionUID = 1L;

  private final HashMap<Long, ISubprogram> subprograms = new HashMap<>();
  private final HashMap<Long, IView> views = new HashMap<>();

  private final ArrayList<IDataProvider> providers = new ArrayList<>(); // memory optimization

  @Override
  public void initialize()
  {
    super.initialize();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.initialize();

    for (IView renderable : getViewsCopy())
      renderable.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.uninitialize();

    for (IView renderable : getViewsCopy())
      renderable.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    for (IView renderable : getViewsCopy())
      renderable.enable();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    for (IView renderable : getViewsCopy())
      renderable.disable();

    for (ISubprogram subprogram : getSubprogramsCopy())
      subprogram.disable();
  }

  @Override
  public List<IView> getViews() { return getViewsCopy(); }

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
  public void bind(IView view)
  {
    if (view.isInitialized())
      throw new IllegalArgumentException("View should be not initialized");

    if (view.isEnabled())
      throw new IllegalArgumentException("View should be disabled");

    view.setBindable(this);

    if (isInitialized())
      view.initialize();

    if (isEnabled())
      view.enable();

    synchronized (views)
    {
      Long id = view.getId();
      if (views.containsKey(id))
        throw new IllegalStateException("View with this id already added (views)");

      views.put(id, view);
    }
  }

  @Override
  public void unbind(IView view)
  {
    view.setBindable(null);

    if (view.isEnabled())
      view.disable();

    if (view.isInitialized())
      view.uninitialize();

    synchronized (views)
    {
      views.remove(view.getId());
    }
  }

  @Override
  public void setViews()
  {
    synchronized (views)
    {
      // copy because provider.set can add views
      providers.clear();
      for (IView view : views.values())
        providers.add(view.getProvider());
    }

    for (IDataProvider provider : providers)
      provider.set();
  }

  protected void enqueueViewEvent(String event)
  {
    for (IView view : views.values())
    {
      IDataProvider provider = view.getProvider();
      provider.enqueue(event);
    }
  }

  protected ArrayList<ISubprogram> getSubprogramsCopy()
  {
    synchronized (subprograms)
    {
      return new ArrayList<>(subprograms.values());
    }
  }

  protected ArrayList<IView> getViewsCopy()
  {
    synchronized (views)
    {
      return new ArrayList<>(views.values());
    }
  }
}
