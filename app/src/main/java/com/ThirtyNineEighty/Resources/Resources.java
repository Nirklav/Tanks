package com.ThirtyNineEighty.Resources;

import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Resources.Entities.FrameBuffer;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Resources.Entities.Image;
import com.ThirtyNineEighty.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.Resources.Sources.HemisphereParticlesSource;
import com.ThirtyNineEighty.Resources.Sources.ISource;

import java.util.ArrayList;

public final class Resources
{
  private final ArrayList<ResourceCache<?>> caches;

  private final ResourceCache<Texture> textureCache;
  private final ResourceCache<Geometry> geometryCache;
  private final ResourceCache<Image> imagesCache;
  private final ResourceCache<PhGeometry> phGeometryCache;
  private final ResourceCache<Description> descriptionCache;
  private final ResourceCache<ArrayList<String>> contentCache;
  private final ResourceCache<FrameBuffer> frameBuffersCache;
  private final ResourceCache<MapDescription> mapsCache;

  public Resources()
  {
    caches = new ArrayList<>();
    caches.add(textureCache = new ResourceCache<>("Textures"));
    caches.add(geometryCache = new ResourceCache<>("Geometries"));
    caches.add(imagesCache = new ResourceCache<>("Images"));
    caches.add(phGeometryCache = new ResourceCache<>("PhGeometry"));
    caches.add(descriptionCache = new ResourceCache<>("Descriptions"));
    caches.add(contentCache = new ResourceCache<>("Contents"));
    caches.add(frameBuffersCache = new ResourceCache<>("FrameBuffers"));
    caches.add(mapsCache = new ResourceCache<>("Maps"));
  }

  public Texture getTexture(ISource<Texture> source) { return textureCache.get(source); }
  public Geometry getGeometry(ISource<Geometry> source) { return geometryCache.get(source); }
  public Image getImage(ISource<Image> source) { return imagesCache.get(source); }
  public PhGeometry getPhGeometry(ISource<PhGeometry> source) { return phGeometryCache.get(source); }
  public Description getDescription(ISource<Description> source) { return descriptionCache.get(source); }
  public ArrayList<String> getContent(ISource<ArrayList<String>> source) { return contentCache.get(source); }
  public FrameBuffer getFrameBuffer(ISource<FrameBuffer> source) { return frameBuffersCache.get(source); }
  public MapDescription getMap(ISource<MapDescription> source) { return mapsCache.get(source); }

  public void preload()
  {
    geometryCache.get(new HemisphereParticlesSource());
  }

  public void reloadCache()
  {
    for(ResourceCache<?> cache : caches)
      cache.reload();
  }

  public void clearCache()
  {
    for(ResourceCache<?> cache : caches)
      cache.clear();
  }

  public String getCacheStatus()
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
