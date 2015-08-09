package com.ThirtyNineEighty.Game.Data;

import com.ThirtyNineEighty.Game.Data.Entities.CampaignEntity;
import com.ThirtyNineEighty.Game.Data.Entities.MapEntity;
import com.ThirtyNineEighty.Game.Data.Entities.TankEntity;
import com.ThirtyNineEighty.Game.Data.Entities.UpgradeEntity;
import com.ThirtyNineEighty.System.GameContext;

public final class GameProgressManager
{
  private DataBase dataBase;

  public void initialize()
  {
    dataBase = new DataBase(GameContext.activity);

    openMap("standard");
    openTank("tank");
  }

  public boolean isMapOpen(String name)
  {
    Entity<MapEntity> map = dataBase.maps.read(name);
    return map != null && map.data.opened;
  }

  public boolean isTankOpen(String name)
  {
    Entity<TankEntity> tank = dataBase.tanks.read(name);
    return tank != null && tank.data.opened;
  }

  public boolean isUpgradeOpen(String name)
  {
    Entity<UpgradeEntity> upgrade = dataBase.upgrades.read(name);
    return upgrade != null && upgrade.data.opened;
  }

  public String getCurrentCampaignMap(String name)
  {
    Entity<CampaignEntity> campaign = dataBase.campaigns.read(name);
    if (campaign == null)
      return null;
    return campaign.data.currentMap;
  }

  public void openMap(String name)
  {
    Entity<MapEntity> map = dataBase.maps.read(name);
    if (map == null)
    {
      dataBase.maps.insert(name, new MapEntity(true));
      return;
    }
    map.data.opened = true;
    dataBase.maps.update(map);
  }

  public void openTank(String name)
  {
    Entity<TankEntity> tank = dataBase.tanks.read(name);
    if (tank == null)
    {
      dataBase.tanks.insert(name, new TankEntity(true));
      return;
    }
    tank.data.opened = true;
    dataBase.tanks.update(tank);
  }

  public void openUpgrade(String name)
  {
    Entity<UpgradeEntity> upgrade = dataBase.upgrades.read(name);
    if (upgrade == null)
    {
      dataBase.upgrades.insert(name, new UpgradeEntity(true));
      return;
    }
    upgrade.data.opened = true;
    dataBase.upgrades.update(upgrade);
  }

  public void setCurrentCampaignMap(String campaignName, String mapName)
  {
    Entity<CampaignEntity> campaign = dataBase.campaigns.read(campaignName);
    if (campaign == null)
    {
      dataBase.campaigns.insert(campaignName, new CampaignEntity(mapName));
      return;
    }
    campaign.data.currentMap = mapName;
    dataBase.campaigns.update(campaign);
  }
}