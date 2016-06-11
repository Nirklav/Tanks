package com.ThirtyNineEighty.Game.Data;

import android.content.Context;

import com.ThirtyNineEighty.Base.Data.DataBase;
import com.ThirtyNineEighty.Base.Data.Table;
import com.ThirtyNineEighty.Game.Data.Entities.*;

public class TanksDataBase
  extends DataBase
{
  public final Table<MapEntity> maps;
  public final Table<TankEntity> tanks;
  public final Table<UpgradeEntity> upgrades;
  public final Table<CampaignEntity> campaigns;

  public TanksDataBase(Context context)
  {
    super(context);

    register(maps = new Table<>(this, "Maps"));
    register(tanks = new Table<>(this, "Tanks"));
    register(upgrades = new Table<>(this, "Upgrades"));
    register(campaigns = new Table<>(this, "Campaigns"));
  }
}