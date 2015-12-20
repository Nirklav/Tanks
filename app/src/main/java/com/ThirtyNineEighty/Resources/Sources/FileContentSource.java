package com.ThirtyNineEighty.Resources.Sources;

import java.util.ArrayList;

public class FileContentSource
  extends FileSerializedSource<ArrayList<String>>
{
  public static final String other = "other";
  public static final String tanks = "tanks";
  public static final String bullets = "bullets";
  public static final String upgrades = "upgrades";
  public static final String decors = "decors";
  public static final String bots = "bots";
  public static final String maps = "maps";

  public FileContentSource(String name)
  {
    super(name, "Content", "ch");
  }
}
