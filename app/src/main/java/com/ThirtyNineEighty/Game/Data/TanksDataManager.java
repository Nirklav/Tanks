package com.ThirtyNineEighty.Game.Data;

import com.ThirtyNineEighty.Base.Data.DataManager;
import com.ThirtyNineEighty.Base.Data.Record;
import com.ThirtyNineEighty.Game.Data.Entities.CampaignEntity;
import com.ThirtyNineEighty.Game.Data.Entities.MapEntity;
import com.ThirtyNineEighty.Game.Data.Entities.TankEntity;
import com.ThirtyNineEighty.Game.Data.Entities.UpgradeEntity;

public class TanksDataManager
  extends DataManager
{
  private TanksDataBase dataBase;

  public TanksDataManager(TanksDataBase dataBase)
  {
    super(dataBase);

    this.dataBase = dataBase;
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

  public void openMap(String name)
  {
    dataBase.maps.save(name, new MapEntity(true));
  }

  public void openTank(String name)
  {
    dataBase.tanks.save(name, new TankEntity(true));
  }

  public void openUpgrade(String name)
  {
    dataBase.upgrades.save(name, new UpgradeEntity(true));
  }

  public void setCurrentCampaignMap(String campaignName, String mapName)
  {
    dataBase.campaigns.save(campaignName, new CampaignEntity(mapName));
  }
}
