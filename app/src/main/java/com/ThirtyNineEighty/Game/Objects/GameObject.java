package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;

public abstract class GameObject
  extends EngineObject
{
  private Characteristic baseCharacteristic;
  private Characteristic characteristic;

  protected GameObject(Characteristic characteristic)
  {
    super(characteristic.initializer);

    this.baseCharacteristic = new Characteristic(characteristic);
    this.characteristic = new Characteristic(characteristic);
  }

  public Characteristic getBaseCharacteristic() { return baseCharacteristic; }
  public Characteristic getCharacteristic() { return characteristic; }
}