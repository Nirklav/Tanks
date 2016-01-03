package com.ThirtyNineEighty.Game.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    maps = new Table<>(this, "Maps");
    tanks = new Table<>(this, "Tanks");
    upgrades = new Table<>(this, "Upgrades");
    campaigns = new Table<>(this, "Campaigns");
  }

  @Override
  public void onCreate(SQLiteDatabase database)
  {
    super.onCreate(database);

    maps.create(database);
    tanks.create(database);
    upgrades.create(database);
    campaigns.create(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
  {
    super.onUpgrade(database, oldVersion, newVersion);
  }
}