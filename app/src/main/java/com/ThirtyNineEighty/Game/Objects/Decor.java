package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Characteristics.CharacteristicFactory;

public class Decor
  extends EngineObject
{
  public Decor(String type)
  {
    super(CharacteristicFactory.get(type).initializer);
  }
}
