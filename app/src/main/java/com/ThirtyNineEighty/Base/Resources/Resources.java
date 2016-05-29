package com.ThirtyNineEighty.Base.Resources;

import com.ThirtyNineEighty.Base.IStatistics;
import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Base.Resources.Entities.FrameBuffer;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.Entities.IResource;
import com.ThirtyNineEighty.Base.Resources.Entities.Image;
import com.ThirtyNineEighty.Base.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Base.Resources.Entities.Texture;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;

import java.util.ArrayList;

public class Resources
  implements IStatistics
{
  private final ArrayList<ResourceCache<?>> caches;

  private final ResourceCache<Texture> textureCache;
  private final ResourceCache<Geometry> geometryCache;
  private final ResourceCache<Image> imagesCache;
  private final ResourceCache<PhGeometry> phGeometryCache;
  private final ResourceCache<Description> descriptionCache;
  private final ResourceCache<ContentNames> contentCache;
  private final ResourceCache<FrameBuffer> frameBuffersCache;

  public Resources()
  {
    caches = new ArrayList<>();

    add(textureCache = new ResourceCache<>("Textures"));
    add(geometryCache = new ResourceCache<>("Geometries"));
    add(imagesCache = new ResourceCache<>("Images"));
    add(phGeometryCache = new ResourceCache<>("PhGeometry"));
    add(descriptionCache = new ResourceCache<>("Descriptions"));
    add(contentCache = new ResourceCache<>("Contents"));
    add(frameBuffersCache = new ResourceCache<>("FrameBuffers"));
  }

  protected <T extends IResource> void add(ResourceCache<T> cache)
  {
    caches.add(cache);
  }

  public Texture getTexture(ISource<Texture> source) { return textureCache.get(source); }
  public Geometry getGeometry(ISource<Geometry> source) { return geometryCache.get(source); }
  public Image getImage(ISource<Image> source) { return imagesCache.get(source); }
  public PhGeometry getPhGeometry(ISource<PhGeometry> source) { return phGeometryCache.get(source); }
  public Description getDescription(ISource<Description> source) { return descriptionCache.get(source); }
  public ContentNames getContent(ISource<ContentNames> source) { return contentCache.get(source); }
  public FrameBuffer getFrameBuffer(ISource<FrameBuffer> source) { return frameBuffersCache.get(source); }

  public void release(Texture resource) { textureCache.release(resource); }
  public void release(Geometry resource) { geometryCache.release(resource); }
  public void release(Image resource) { imagesCache.release(resource); }
  public void release(PhGeometry resource) { phGeometryCache.release(resource); }
  public void release(Description resource) { descriptionCache.release(resource); }
  public void release(ContentNames resource) { contentCache.release(resource); }
  public void release(FrameBuffer resource) { frameBuffersCache.release(resource); }

  public void preload()
  {
  }

  public void reloadCache()
  {
    for(ResourceCache<?> cache : caches)
      cache.reload();
  }

  public int clearUnusedCache()
  {
    int count = 0;
    for (ResourceCache<?> cache : caches)
      count += cache.clearUnused();

    return count;
  }

  public void clearCache()
  {
    for(ResourceCache<?> cache : caches)
      cache.clear();
  }

  public String getStatistics()
  {
    StringBuilder info = new StringBuilder(20 * caches.size());

    for (ResourceCache<?> cache : caches)
      info.append(cache.getName())
          .append(": ")
          .append(cache.getSize())
          .append('\n');

    return info.toString();
  }
}
