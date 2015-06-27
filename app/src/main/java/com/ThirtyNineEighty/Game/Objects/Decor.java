package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Resources.Sources.FileCharacteristicSource;
import com.ThirtyNineEighty.System.GameContext;

public class Decor
  extends EngineObject
{
  public Decor(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileCharacteristicSource(type)).initializer);
  }
}
