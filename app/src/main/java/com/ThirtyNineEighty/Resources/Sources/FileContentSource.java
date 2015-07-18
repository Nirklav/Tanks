package com.ThirtyNineEighty.Resources.Sources;

import java.util.ArrayList;

public class FileContentSource
  extends FileSerializedSource<ArrayList<String>>
{
  public FileContentSource(String name)
  {
    super(name, "Content");
  }
}
