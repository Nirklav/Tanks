package com.ThirtyNineEighty.Resources.Sources;

import com.ThirtyNineEighty.Common.Serializer;
import com.ThirtyNineEighty.Resources.Entities.Image;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.Resources.Entities.ImageDescription;
import com.ThirtyNineEighty.System.GameContext;

public class FileImageSource
  implements ISource<Image>
{
  private final String name;

  public FileImageSource(String imageName)
  {
    name = imageName;
  }

  @Override
  public String getName() { return name; }

  @Override
  public Image load()
  {
    String fileName = getImageFileName(name);
    ImageDescription description = Serializer.Deserialize(fileName);
    Texture texture = GameContext.resources.getTexture(new FileTextureSource(description.textureName, false));

    return new Image(texture, description.coordinates);
  }

  @Override
  public void reload(Image image)
  {
    String fileName = getImageFileName(name);
    ImageDescription description = Serializer.Deserialize(fileName);
    Texture texture = GameContext.resources.getTexture(new FileTextureSource(description.textureName, false));

    image.setTexture(texture);
    image.setCoordinates(description.coordinates);
  }

  @Override
  public void release(Image image)
  {
    image.setTexture(null);
    image.setCoordinates(null);
  }

  private static String getImageFileName(String name)
  {
    return String.format("Images/%s.img", name);
  }
}