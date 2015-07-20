package com.ThirtyNineEighty.Game.Objects.Properties;

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

  public GameProperties(String bullet, List<String> upgrades)
  {
    this.bullet = bullet;

    if (upgrades != null)
      this.upgrades = new ArrayList<>(upgrades);
  }

  public boolean needKill() { return needKill; }
  public String getBullet() { return bullet; }
  public List<String> getUpgrades() { return upgrades; }
}
