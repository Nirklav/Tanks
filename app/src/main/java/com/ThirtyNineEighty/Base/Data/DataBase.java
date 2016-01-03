package com.ThirtyNineEighty.Base.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ThirtyNineEighty.Base.Data.Entities.SavedWorldEntity;

public class DataBase
  extends SQLiteOpenHelper
{
  public final Table<SavedWorldEntity> worlds;

  public DataBase(Context context)
  {
    super(context, "Data", null, 1);

    worlds = new Table<>(this, "SavedWorlds");
  }

  @Override
  public void onCreate(SQLiteDatabase database)
  {
    worlds.create(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
  {

  }
}