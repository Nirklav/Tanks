package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;

public class FileContentSource
  extends FileSerializedSource<ContentNames>
{
  public static final String other = "other";
  public static final String tanks = "tanks";
  public static final String bullets = "bullets";
  public static final String upgrades = "upgrades";
  public static final String decors = "decors";
  public static final String bots = "bots";
  public static final String maps = "maps";
  public static final String bonuses = "bonuses";

  public FileContentSource(String name)
  {
    super(name, "Content", "ch");
  }
}
