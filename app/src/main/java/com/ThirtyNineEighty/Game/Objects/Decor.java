package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;

public class Decor
  extends EngineObject
{
  public Decor(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileDescriptionSource(type)), new Properties());
  }
}
