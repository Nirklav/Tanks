package com.ThirtyNineEighty.Renderable.Resources;

import android.opengl.GLES20;

import java.util.HashMap;

public final class RenderableResources
{
  private HashMap<String, Container<Texture>> texturesCache = new HashMap<String, Container<Texture>>();
  private HashMap<GeometryKey, Container<Geometry>> geometryCache = new HashMap<GeometryKey, Container<Geometry>>();

  private HashMap<String, Image> imagesCache = new HashMap<String, Image>();

  public Texture getTexture(String name, boolean generateMipmap)
  {
    if (texturesCache.containsKey(name))
      return texturesCache.get(name).resource;

    TextureSource source = new TextureSource(name, generateMipmap);
    Texture texture = source.load();

    texturesCache.put(name, new Container<Texture>(texture, source));
    return texture;
  }

  public Geometry getGeometry(String name, int numOfTriangles, float[] bufferData) { return getGeometry(name, numOfTriangles, bufferData, MeshMode.Static); }
  public Geometry getGeometry(String name, int numOfTriangles, float[] bufferData, MeshMode mode)
  {
    GeometryKey key = new GeometryKey(name, mode);
    if (geometryCache.containsKey(key))
      return geometryCache.get(key).resource;

    StaticGeometrySource source = new StaticGeometrySource(bufferData, numOfTriangles, mode);
    Geometry geometry = source.load();

    geometryCache.put(key, new Container<Geometry>(geometry, source));
    return geometry;
  }

  public Geometry getGeometry(String name)
  {
    GeometryKey key = new GeometryKey(name, MeshMode.Static);
    if (geometryCache.containsKey(key))
      return geometryCache.get(key).resource;

    FileGeometrySource source = new FileGeometrySource(name);
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

      int oldHandle = container.resource.getHandle();
      if (GLES20.glIsTexture(oldHandle))
        GLES20.glDeleteTextures(1, new int[] { oldHandle }, 0);

      container.reload();
    }
  }

  private void reloadGeometry()
  {
    for (GeometryKey key : geometryCache.keySet())
    {
      if (key.mode == MeshMode.Dynamic)
        continue;

      Container<Geometry> container = geometryCache.get(key);

      int oldHandle = container.resource.getHandle();
      if (GLES20.glIsBuffer(oldHandle))
        GLES20.glDeleteBuffers(1, new int[] { oldHandle }, 0);

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
    int counter = 0;
    int[] textures = new int[texturesCache.size()];
    for(Container<Texture> container : texturesCache.values())
    {
      int handle = container.resource.getHandle();
      if (GLES20.glIsTexture(handle))
        textures[counter++] = handle;
    }

    if (counter > 0)
      GLES20.glDeleteTextures(counter, textures, 0);

    texturesCache.clear();
  }

  private void clearGeometryCache()
  {
    int counter = 0;
    int[] buffers = new int[geometryCache.size()];
    for(Container<Geometry> geometry : geometryCache.values())
    {
      MeshMode mode = geometry.resource.getMode();
      if (mode == MeshMode.Dynamic)
        continue;

      int handle = geometry.resource.getHandle();
      if (GLES20.glIsBuffer(handle))
        buffers[counter++] = geometry.resource.getHandle();
    }

    if (counter > 0)
      GLES20.glDeleteBuffers(counter, buffers, 0);

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
  }
}
