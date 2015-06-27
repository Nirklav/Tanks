package com.ThirtyNineEighty.Game.Data;

public class Entity<T>
{
  public final String name;
  public final T data;

  public Entity(String name, T data)
  {
    this.name = name;
    this.data = data;
  }
}
