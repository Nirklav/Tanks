package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Serializer;
import com.ThirtyNineEighty.Base.Resources.Entities.IResource;

public class FileSerializedSource<TResource extends IResource>
  implements ISource<TResource>
{
  private final String name;
  private final String path;
  private final String extension;

  public FileSerializedSource(String name, String path, String extension)
  {
    this.name = name;
    this.path = path;
    this.extension = extension;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public TResource load()
  {
    return Serializer.Deserialize(String.format("%s/%s.%s", path, name, extension));
  }

  @Override
  public void reload(TResource characteristic) { }

  @Override
  public void release(TResource characteristic) { }
}
