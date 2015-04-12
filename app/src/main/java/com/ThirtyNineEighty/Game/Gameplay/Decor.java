package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;

public class Decor
  extends EngineObject
{
  public Decor(String type)
  {
    super(CharacteristicFactory.get(type).initializer);
  }
}
