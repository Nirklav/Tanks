package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;

public class FileDescriptionSource
  extends FileSerializedSource<Description>
{
  public FileDescriptionSource(String name)
  {
    super(name, "Descriptions", "ch");
  }
}
