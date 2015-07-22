package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public class GameStartArgs
{
  private String mapName;
  private String tankName;
  private GameProperties properties;

  public GameStartArgs()
  {
    ArrayList<String> maps = GameContext.resources.getContent(new FileContentSource("maps"));
    ArrayList<String> tanks = GameContext.resources.getContent(new FileContentSource("tanks"));
    ArrayList<String> bullets = GameContext.resources.getContent(new FileContentSource("bullets"));

    mapName = maps.get(0);
    tankName = tanks.get(0);

    properties = new GameProperties();
    properties.setBullet(bullets.get(0));
  }

  public String getMapName() { return mapName; }
  public void setMapName(String value) { mapName = value; }

  public String getTankName() { return tankName; }
  public void setTankName(String value) { tankName = value; }

  public GameProperties getProperties() { return properties; }
  public void setProperties(GameProperties value) { properties = value; }
}
