package com.ThirtyNineEighty.Renderable.Resources;

public final class RenderableResources
{
  private ResourceCache<Texture> textureCache = new ResourceCache<Texture>();
  private ResourceCache<Geometry> geometryCache = new ResourceCache<Geometry>();
  private ResourceCache<Image> imagesCache = new ResourceCache<Image>();

  public Texture getTexture(ISource<Texture> source) { return textureCache.get(source); }
  public Geometry getGeometry(ISource<Geometry> source) { return geometryCache.get(source); }
  public Image getImage(ISource<Image> source) { return imagesCache.get(source); }

  public void reloadCache()
  {
    textureCache.reload();
    geometryCache.reload();
    imagesCache.reload();
  }

  public void clearCache()
  {
    textureCache.clear();
    geometryCache.clear();
    imagesCache.clear();
  }

  public String getCacheStatus()
  {
    return String.format("Textures: %d\n", textureCache.getSize())
         + String.format("Geometries: %d\n", geometryCache.getSize())
         + String.format("Images: %d", imagesCache.getSize());
  }
}
