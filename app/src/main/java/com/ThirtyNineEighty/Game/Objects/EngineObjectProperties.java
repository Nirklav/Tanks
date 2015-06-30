package com.ThirtyNineEighty.Game.Objects;

public class EngineObjectProperties
{
  public final boolean removeOnCollide;

  public EngineObjectProperties(EngineObjectDescription description)
  {
    removeOnCollide = description.removeOnCollide;
  }
}
