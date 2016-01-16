package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Base.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.Game.TanksContext;

public class GameStartArgs
{
  private String mapName;
  private String tankName;
  private GameProperties properties;

  public GameStartArgs()
  {
    ContentNames maps = TanksContext.resources.getContent(new FileContentSource(FileContentSource.maps));
    ContentNames tanks = TanksContext.resources.getContent(new FileContentSource(FileContentSource.tanks));
    ContentNames bullets = TanksContext.resources.getContent(new FileContentSource(FileContentSource.bullets));

    mapName = maps.names.get(0);
    tankName = tanks.names.get(0);

    properties = new GameProperties();
    properties.setBullet(bullets.names.get(0));

    TanksContext.resources.release(maps);
    TanksContext.resources.release(tanks);
    TanksContext.resources.release(bullets);
  }

  public String getMapName() { return mapName; }
  public void setMapName(String value) { mapName = value; }

  public String getTankName() { return tankName; }
  public void setTankName(String value) { tankName = value; }

  public GameProperties getProperties() { return properties; }
  public void setProperties(GameProperties value) { properties = value; }
}
