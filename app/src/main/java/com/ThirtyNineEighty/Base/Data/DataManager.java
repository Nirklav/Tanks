package com.ThirtyNineEighty.Base.Data;

import com.ThirtyNineEighty.Base.Data.Entities.SavedWorldEntity;
import com.ThirtyNineEighty.Base.Worlds.IWorld;

public class DataManager
{
  private final DataBase dataBase;

  public DataManager(DataBase dataBase)
  {
    this.dataBase = dataBase;
  }

  public IWorld getSavedWorld(String name)
  {
    Record<SavedWorldEntity> record = dataBase.worlds.read(name);
    return record == null ? null : record.data.world;
  }

  public void saveWorld(String name, IWorld world)
  {
    dataBase.worlds.save(name, new SavedWorldEntity(world));
  }

  public void deleteWorld(String name)
  {
    dataBase.worlds.delete(name);
  }

  public void close()
  {
    dataBase.close();
  }
}