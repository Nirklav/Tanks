package com.ThirtyNineEighty.Game.Resources;

import com.ThirtyNineEighty.Base.Resources.ResourceCache;
import com.ThirtyNineEighty.Base.Resources.Resources;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;
import com.ThirtyNineEighty.Base.Resources.Sources.RandomParticlesSource;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;

public class TanksResources
  extends Resources
{
  private final ResourceCache<MapDescription> mapsCache;

  public TanksResources()
  {
    super();

    add(mapsCache = new ResourceCache<>("Maps"));
  }

  public MapDescription getMap(ISource<MapDescription> source) { return mapsCache.get(source); }

  public void release(MapDescription resource) { mapsCache.release(resource); }

  @Override
  public void preload()
  {
    super.preload();

    getGeometry(new RandomParticlesSource(60));
  }
}
