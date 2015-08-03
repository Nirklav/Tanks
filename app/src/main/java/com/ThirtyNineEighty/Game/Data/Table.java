package com.ThirtyNineEighty.Game.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ThirtyNineEighty.Common.Serializer;

import java.util.ArrayList;
import java.util.Collection;

public class Table<T>
{
  private String name;
  private SQLiteOpenHelper helper;

  public Table(SQLiteOpenHelper helper, String name)
  {
    this.name = name;
    this.helper = helper;
  }

  public void create(SQLiteDatabase database)
  {
    database.execSQL(String.format("CREATE TABLE %s (Name Text PRIMARY KEY, Data BLOB);", name));
  }

  public Entity<T> read(String entityName)
  {
    SQLiteDatabase database = helper.getReadableDatabase();
    Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE Name = ?", name), new String[]{ entityName });

    if (!cursor.moveToFirst())
    {
      cursor.close();
      return null;
    }

    int dataIndex = cursor.getColumnIndex("Data");
    byte[] blob = cursor.getBlob(dataIndex);
    T data = Serializer.Deserialize(blob);

    cursor.close();
    database.close();
    helper.close();
    return new Entity<>(entityName, data);
  }

  public Collection<Entity<T>> readAll()
  {
    SQLiteDatabase database = helper.getReadableDatabase();
    Cursor cursor = database.query(name, null, null, null, null, null, null);

    if (!cursor.moveToFirst())
    {
      cursor.close();
      return null;
    }

    int nameIndex = cursor.getColumnIndex("Name");
    int dataIndex = cursor.getColumnIndex("Data");

    ArrayList<Entity<T>> result = new ArrayList<>();
    while (true)
    {
      String name = cursor.getString(nameIndex);
      byte[] blob = cursor.getBlob(dataIndex);
      T data = Serializer.Deserialize(blob);
      result.add(new Entity<>(name, data));

      if (!cursor.moveToNext())
        break;
    }

    cursor.close();
    database.close();
    helper.close();
    return result;
  }

  public long insert(Entity<T> entity) { return insert(entity.name, entity.data); }
  public long insert(String entityName, T data)
  {
    SQLiteDatabase database = helper.getWritableDatabase();
    ContentValues values = new ContentValues(2);
    values.put("Name", entityName);
    values.put("Data", Serializer.Serialize(data));

    long id = database.insertOrThrow(name, null, values);

    database.close();
    helper.close();

    return id;
  }

  public int update(Entity<T> entity) { return update(entity.name, entity.data); }
  public int update(String entityName, T data)
  {
    SQLiteDatabase database = helper.getWritableDatabase();
    ContentValues values = new ContentValues(2);
    values.put("Name", entityName);
    values.put("Data", Serializer.Serialize(data));

    int count = database.update(name, values, "Name = ?", new String[]{ entityName });

    database.close();
    helper.close();

    return count;
  }
}
