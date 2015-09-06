package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;

public class FileDescriptionSource
  extends FileSerializedSource<Description>
{
  public FileDescriptionSource(String name)
  {
    super(name, "Descriptions");
  }
}
