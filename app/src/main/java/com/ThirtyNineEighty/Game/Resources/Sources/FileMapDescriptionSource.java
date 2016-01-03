package com.ThirtyNineEighty.Game.Resources.Sources;

import com.ThirtyNineEighty.Base.Resources.Sources.FileSerializedSource;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;

public class FileMapDescriptionSource
  extends FileSerializedSource<MapDescription>
{
  public FileMapDescriptionSource(String name)
  {
    super(name, "Maps", "map");
  }
}
