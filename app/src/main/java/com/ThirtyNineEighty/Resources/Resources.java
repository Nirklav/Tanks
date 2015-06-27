package com.ThirtyNineEighty.Resources;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Resources.Entities.Image;
import com.ThirtyNineEighty.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.Resources.Sources.ISource;

public final class Resources
{
  private ResourceCache<Texture> textureCache = new ResourceCache<>();
  private ResourceCache<Geometry> geometryCache = new ResourceCache<>();
  private ResourceCache<Image> imagesCache = new ResourceCache<>();
  private ResourceCache<PhGeometry> phGeometryCache = new ResourceCache<>();
  private ResourceCache<Characteristic> characteristicCache = new ResourceCache<>();

  public Texture getTexture(ISource<Texture> source) { return textureCache.get(source); }
  public Geometry getGeometry(ISource<Geometry> source) { return geometryCache.get(source); }
  public Image getImage(ISource<Image> source) { return imagesCache.get(source); }
  public PhGeometry getPhGeometry(ISource<PhGeometry> source) { return phGeometryCache.get(source); }
  public Characteristic getCharacteristic(ISource<Characteristic> source) { return characteristicCache.get(source); }

  public void reloadCache()
  {
    textureCache.reload();
    geometryCache.reload();
    imagesCache.reload();
    phGeometryCache.reload();
    characteristicCache.reload();
  }

  public void clearCache()
  {
    textureCache.clear();
    geometryCache.clear();
    imagesCache.clear();
    imagesCache.clear();
    characteristicCache.clear();
  }

  public String getCacheStatus()
  {
    return String.format("Textures: %d\n", textureCache.getSize())
         + String.format("Geometries: %d\n", geometryCache.getSize())
         + String.format("Images: %d\n", imagesCache.getSize())
         + String.format("PhGeometry: %d\n", phGeometryCache.getSize())
         + String.format("Characteristics: %d\n", characteristicCache.getSize());
  }
}
