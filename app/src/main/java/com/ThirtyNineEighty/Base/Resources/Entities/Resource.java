package com.ThirtyNineEighty.Base.Resources.Entities;

public abstract class Resource
  implements IResource
{
  private static final long serialVersionUID = 1L;

  private String name;

  public Resource(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
