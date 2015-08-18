package com.ThirtyNineEighty.Game.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ThirtyNineEighty.Game.Data.Entities.CampaignEntity;
import com.ThirtyNineEighty.Game.Data.Entities.MapEntity;
import com.ThirtyNineEighty.Game.Data.Entities.SavedGameEntity;
import com.ThirtyNineEighty.Game.Data.Entities.TankEntity;
import com.ThirtyNineEighty.Game.Data.Entities.UpgradeEntity;

public class DataBase extends SQLiteOpenHelper
{
  public final Table<MapEntity> maps;
  public final Table<TankEntity> tanks;
  public final Table<UpgradeEntity> upgrades;
  public final Table<CampaignEntity> campaigns;
  public final Table<SavedGameEntity> savedGames;

  public DataBase(Context context)
  {
    super(context, "Data", null, 1);

    maps = new Table<>(this, "Maps");
    tanks = new Table<>(this, "Tanks");
    upgrades = new Table<>(this, "Upgrades");
    campaigns = new Table<>(this, "Campaigns");
    savedGames = new Table<>(this, "SavedGames");
  }

  @Override
  public void onCreate(SQLiteDatabase database)
  {
    maps.create(database);
    tanks.create(database);
    upgrades.create(database);
    campaigns.create(database);
    savedGames.create(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
  {

  }
}