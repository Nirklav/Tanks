package com.ThirtyNineEighty.Base.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ThirtyNineEighty.Base.Common.Serializer;

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

  public Record<T> read(String entityName)
  {
    SQLiteDatabase database = helper.getReadableDatabase();
    Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE Name = ?;", name), new String[] { entityName });

    if (!cursor.moveToFirst())
    {
      cursor.close();
      return null;
    }

    int dataIndex = cursor.getColumnIndex("Data");
    byte[] blob = cursor.getBlob(dataIndex);
    T data = Serializer.Deserialize(blob);

    cursor.close();
    return new Record<>(entityName, data);
  }

  public Collection<Record<T>> readAll()
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

    ArrayList<Record<T>> result = new ArrayList<>();
    while (true)
    {
      String name = cursor.getString(nameIndex);
      byte[] blob = cursor.getBlob(dataIndex);
      T data = Serializer.Deserialize(blob);
      result.add(new Record<>(name, data));

      if (!cursor.moveToNext())
        break;
    }

    cursor.close();
    return result;
  }

  public void save(Record<T> record) { save(record.name, record.data); }
  public void save(String entityName, T data)
  {
    Record<T> record = read(entityName);
    if (record == null)
    {
      insert(entityName, data);
      return;
    }
    update(entityName, data);
  }

  public long insert(Record<T> record) { return insert(record.name, record.data); }
  public long insert(String entityName, T data)
  {
    SQLiteDatabase database = helper.getWritableDatabase();
    ContentValues values = new ContentValues(2);
    values.put("Name", entityName);
    values.put("Data", Serializer.Serialize(data));

    return database.insertOrThrow(name, null, values);
  }

  public int update(Record<T> record) { return update(record.name, record.data); }
  public int update(String entityName, T data)
  {
    SQLiteDatabase database = helper.getWritableDatabase();
    ContentValues values = new ContentValues(2);
    values.put("Name", entityName);
    values.put("Data", Serializer.Serialize(data));

    return database.update(name, values, "Name = ?", new String[]{ entityName });
  }

  public int delete(String entityName)
  {
    SQLiteDatabase database = helper.getWritableDatabase();
    return database.delete(name, "Name = ?", new String[] { entityName });
  }
}
