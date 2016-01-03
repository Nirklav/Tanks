package com.ThirtyNineEighty.Base;

import java.util.ArrayList;

public class BindableHost<T extends IBindable>
  extends Bindable
{
  private static final long serialVersionUID = 1L;

  protected final ArrayList<T> objects = new ArrayList<>();

  public void add(T bindable)
  {
    if (bindable.isInitialized())
      throw new IllegalArgumentException("Bindable should be not initialized");

    if (bindable.isEnabled())
      throw new IllegalArgumentException("Bindable should be disabled");

    if (isInitialized())
      bindable.initialize();

    if (isEnabled())
      bindable.enable();

    synchronized (objects)
    {
      objects.add(bindable);
    }
  }

  public void remove(T bindable)
  {
    if (bindable.isEnabled())
      bindable.disable();

    if (bindable.isInitialized())
      bindable.uninitialize();

    synchronized (objects)
    {
      objects.remove(bindable);
    }
  }

  @Override
  public void initialize()
  {
    super.initialize();

    for (IBindable bindable : getCopy())
      bindable.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    for (IBindable bindable : getCopy())
      bindable.uninitialize();
  }

  @Override
  public void enable()
  {
    super.enable();

    for (IBindable bindable : getCopy())
      bindable.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    for (IBindable bindable : getCopy())
      bindable.disable();
  }

  @Override
  public void setViews()
  {
    super.setViews();

    for (IBindable bindable : getCopy())
      bindable.setViews();
  }

  private ArrayList<T> getCopy()
  {
    synchronized (objects)
    {
      return new ArrayList<>(objects);
    }
  }
}
