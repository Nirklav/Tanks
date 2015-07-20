package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;

public class FileDescriptionSource
  extends FileSerializedSource<GameDescription>
{
  public FileDescriptionSource(String name)
  {
    super(name, "Descriptions");
  }
}
