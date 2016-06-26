package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Resources.Entities.Image;

public class FileImageSource
  extends FileSerializedSource<Image>
{
  public FileImageSource(String name)
  {
    super(name, "Images", "img");
  }
}