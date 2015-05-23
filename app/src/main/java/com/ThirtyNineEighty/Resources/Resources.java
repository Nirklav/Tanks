package com.ThirtyNineEighty.Resources;

public final class Resources
{
  private ResourceCache<Texture> textureCache = new ResourceCache<>();
  private ResourceCache<Geometry> geometryCache = new ResourceCache<>();
  private ResourceCache<Image> imagesCache = new ResourceCache<>();
  private ResourceCache<PhGeometry> phGeometryCache = new ResourceCache<>();

  public Texture getTexture(ISource<Texture> source) { return textureCache.get(source); }
  public Geometry getGeometry(ISource<Geometry> source) { return geometryCache.get(source); }
  public Image getImage(ISource<Image> source) { return imagesCache.get(source); }
  public PhGeometry getPhGeometry(ISource<PhGeometry> source) { return phGeometryCache.get(source); }

  public void reloadCache()
  {
    textureCache.reload();
    geometryCache.reload();
    imagesCache.reload();
    phGeometryCache.reload();
  }

  public void clearCache()
  {
    textureCache.clear();
    geometryCache.clear();
    imagesCache.clear();
    imagesCache.reload();
  }

  public String getCacheStatus()
  {
    return String.format("Textures: %d\n", textureCache.getSize())
         + String.format("Geometries: %d\n", geometryCache.getSize())
         + String.format("Images: %d", imagesCache.getSize())
         + String.format("PhGeometry: %d", phGeometryCache.getSize());
  }
}
