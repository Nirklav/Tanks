package com.ThirtyNineEighty.Game.Data;

import com.ThirtyNineEighty.Game.Data.Entities.*;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;

public final class GameProgressManager
{
  private DataBase dataBase;

  public void initialize()
  {
    dataBase = new DataBase(GameContext.activity);

    openMap("standard");
    openMap("move test");
    openTank("without bots");
  }

  public boolean isMapOpen(String name)
  {
    Record<MapEntity> record = dataBase.maps.read(name);
    return record != null && record.data.opened;
  }

  public boolean isTankOpen(String name)
  {
    Record<TankEntity> record = dataBase.tanks.read(name);
    return record != null && record.data.opened;
  }

  public boolean isUpgradeOpen(String name)
  {
    Record<UpgradeEntity> record = dataBase.upgrades.read(name);
    return record != null && record.data.opened;
  }

  public String getCurrentCampaignMap(String name)
  {
    Record<CampaignEntity> record = dataBase.campaigns.read(name);
    return record == null ? null : record.data.currentMap;
  }

  public SavedWorldEntity getSavedWorld(String name)
  {
    Record<SavedWorldEntity> record = dataBase.worlds.read(name);
    return record == null ? null : record.data;
  }

  public void openMap(String name) { dataBase.maps.save(name, new MapEntity(true)); }

  public void openTank(String name) { dataBase.tanks.save(name, new TankEntity(true)); }

  public void openUpgrade(String name) { dataBase.upgrades.save(name, new UpgradeEntity(true)); }

  public void setCurrentCampaignMap(String campaignName, String mapName) { dataBase.campaigns.save(campaignName, new CampaignEntity(mapName)); }

  public void saveWorld(String name, String mapName, IWorld world)
  {
    if (world.isInitialized())
      throw new IllegalStateException("can't save initialized world");

    dataBase.worlds.save(name, new SavedWorldEntity(mapName, world));
  }

  public void deleteWorld(String name) { dataBase.worlds.delete(name); }

  public void close()
  {
    dataBase.close();
  }
}