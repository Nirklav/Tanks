package com.ThirtyNineEighty.Base.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ThirtyNineEighty.Base.Data.Entities.SavedWorldEntity;

import java.util.ArrayList;

public class DataBase
  extends SQLiteOpenHelper
{
  private final ArrayList<Table<?>> tables;
  public final Table<SavedWorldEntity> worlds;

  public DataBase(Context context)
  {
    super(context, "Data", null, 1);

    tables = new ArrayList<>();
    register(worlds = new Table<>(this, "SavedWorlds"));
  }

  protected void register(Table<?> table)
  {
    tables.add(table);
  }

  @Override
  public void onCreate(SQLiteDatabase database)
  {
    for (Table<?> table : tables)
      table.create(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
  {

  }
}