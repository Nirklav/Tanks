package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Serializer;
import com.ThirtyNineEighty.Base.Resources.Entities.Image;

public class FileImageSource
  implements ISource<Image>
{
  private final String name;

  public FileImageSource(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Image load()
  {
    String fileName = getImageFileName(name);
    return Serializer.Deserialize(fileName);
  }

  @Override
  public void reload(Image image)
  {

  }

  @Override
  public void release(Image image)
  {

  }

  private static String getImageFileName(String name)
  {
    return String.format("Images/%s.img", name);
  }
}