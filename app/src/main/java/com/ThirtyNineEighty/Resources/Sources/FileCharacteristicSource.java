package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Helpers.Serializer;
import com.ThirtyNineEighty.Resources.Entities.Characteristic;

public class FileCharacteristicSource
  implements ISource<Characteristic>
{
  private final String name;

  public FileCharacteristicSource(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Characteristic load()
  {
    return Serializer.Deserialize(String.format("Characteristics/%s.ch", name));
  }

  @Override
  public void reload(Characteristic characteristic) { }

  @Override
  public void release(Characteristic characteristic) { }
}
