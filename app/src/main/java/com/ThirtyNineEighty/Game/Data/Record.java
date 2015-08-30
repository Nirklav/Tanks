package com.ThirtyNineEighty.Game.Data;

public class Record<T>
{
  public String name;
  public T data;

  public Record(String name)
  {
    this.name = name;
  }

  public Record(String name, T data)
  {
    this.name = name;
    this.data = data;
  }
}
