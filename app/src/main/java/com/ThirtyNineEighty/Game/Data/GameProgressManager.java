package com.ThirtyNineEighty.Game.Data;

import com.ThirtyNineEighty.Game.Data.Entities.*;
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

  public SavedGameEntity getSavedGame(String name)
  {
    Record<SavedGameEntity> record = dataBase.savedGames.read(name);
    return record == null ? null : record.data;
  }

  public void openMap(String name) { dataBase.maps.save(name, new MapEntity(true)); }
  public void openTank(String name) { dataBase.tanks.save(name, new TankEntity(true)); }
  public void openUpgrade(String name) { dataBase.upgrades.save(name, new UpgradeEntity(true)); }
  public void setCurrentCampaignMap(String campaignName, String mapName) { dataBase.campaigns.save(campaignName, new CampaignEntity(mapName)); }
  public void saveGame(String name, SavedGameEntity savedGame) { dataBase.savedGames.save(name, savedGame); }

  public void close()
  {
    dataBase.close();
  }
}