package com.ThirtyNineEighty.Renderable.Resources;

import java.util.HashMap;

public final class RenderableResources
{
  private HashMap<String, Container<Texture>> texturesCache = new HashMap<String, Container<Texture>>();
  private HashMap<GeometryKey, Container<Geometry>> geometryCache = new HashMap<GeometryKey, Container<Geometry>>();

  private HashMap<String, Image> imagesCache = new HashMap<String, Image>();

  public Texture getTexture(ISource<Texture> source)
  {
    Container<Texture> container = texturesCache.get(source.getName());
    if (container != null)
      return container.resource;

    Texture texture = source.load();
    texturesCache.put(source.getName(), new Container<Texture>(texture, source));
    return texture;
  }

  public Geometry getGeometry(GeometrySource source)
  {
    GeometryKey key = new GeometryKey(source.getName(), source.getMode());
    Container<Geometry> container = geometryCache.get(key);
    if (container != null)
      return container.resource;

    Geometry geometry = source.load();
    geometryCache.put(key, new Container<Geometry>(geometry, source));
    return geometry;
  }

  public void reloadCache()
  {
    reloadTextures();
    reloadGeometry();
  }

  private void reloadTextures()
  {
    for (String name : texturesCache.keySet())
    {
      Container<Texture> container = texturesCache.get(name);
      container.reload();
    }
  }

  private void reloadGeometry()
  {
    for (GeometryKey key : geometryCache.keySet())
    {
      Container<Geometry> container = geometryCache.get(key);
      container.reload();
    }
  }

  public void clearCache()
  {
    clearTexturesCache();
    clearGeometryCache();
  }

  private void clearTexturesCache()
  {
    for(Container<Texture> container : texturesCache.values())
      container.release();

    texturesCache.clear();
  }

  private void clearGeometryCache()
  {
    for(Container<Geometry> container : geometryCache.values())
      container.release();

    geometryCache.clear();
  }

  private static class GeometryKey
  {
    public final String name;
    public final MeshMode mode;

    public GeometryKey(String name, MeshMode mode)
    {
      this.name = name;
      this.mode = mode;
    }

    @Override
    public int hashCode() { return (name.hashCode() * 397) ^ mode.hashCode(); }

    @Override
    public boolean equals(Object o)
    {
      if (o == null)
        return false;

      if (this == o)
        return true;

      if (!(o instanceof GeometryKey))
        return false;

      GeometryKey other = (GeometryKey)o;
      return
        name.equals(other.name) &&
        mode.equals(other.mode);
    }
  }

  private static class Container<TResource>
  {
    public final TResource resource;
    public final ISource<TResource> source;

    public Container(TResource res, ISource<TResource> src)
    {
      resource = res;
      source = src;
    }

    public void reload()
    {
      source.reload(resource);
    }

    public void release()
    {
      source.release(resource);
    }
  }
}
