package com.ThirtyNineEighty.Game.Objects.Properties;

import com.ThirtyNineEighty.Base.Objects.Properties.Properties;

import java.util.ArrayList;
import java.util.List;

public class GameProperties
  extends Properties
{
  private static final long serialVersionUID = 1L;

  private boolean needKill;
  private String bullet;
  private List<String> upgrades;

  public GameProperties()
  { }

  public GameProperties(boolean ignoreOnMap)
  {
    setIgnoreOnMap(ignoreOnMap);
  }

  public GameProperties(String bullet, List<String> upgrades)
  {
    this.bullet = bullet;

    this.upgrades = upgrades != null
      ? new ArrayList<>(upgrades)
      : new ArrayList<String>();
  }

  public boolean needKill() { return needKill; }

  public String getBullet() { return bullet; }
  public void setBullet(String value) { bullet = value; }

  public List<String> getUpgrades() { return upgrades; }
  public void setUpgrades(List<String> value) { upgrades = value; }
}
