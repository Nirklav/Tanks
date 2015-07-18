package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;

public class FileCharacteristicSource
  extends FileSerializedSource<Characteristic>
{
  public FileCharacteristicSource(String name)
  {
    super(name, "Characteristics");
  }
}
