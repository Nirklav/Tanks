package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Common.Serializer;

public class FileSerializedSource<TResource>
  implements ISource<TResource>
{
  private final String name;
  private final String path;

  public FileSerializedSource(String name, String path)
  {
    this.name = name;
    this.path = path;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public TResource load()
  {
    return Serializer.Deserialize(String.format("%s/%s.ch", path, name));
  }

  @Override
  public void reload(TResource characteristic) { }

  @Override
  public void release(TResource characteristic) { }
}
