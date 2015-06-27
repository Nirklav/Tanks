package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;

import java.util.ArrayList;

public abstract class GameObject
  extends EngineObject
{
  private Characteristic baseCharacteristic;
  private Characteristic characteristic;
  private ArrayList<IUpgrade> upgrades;

  protected GameObject(Characteristic characteristic)
  {
    super(characteristic.initializer);

    this.upgrades = new ArrayList<>();
    this.baseCharacteristic = new Characteristic(characteristic);
    this.characteristic = new Characteristic(characteristic);
  }

  protected void setCurrentCharacteristics()
  {
    characteristic = new Characteristic(baseCharacteristic);

    for (IUpgrade upgrade : upgrades)
      upgrade.doUpgrade(characteristic);
  }

  public void addUpgrade(IUpgrade upgrade)
  {
    upgrades.add(upgrade);
    setCurrentCharacteristics();
  }

  public void removeUpgrade(IUpgrade upgrade)
  {
    upgrades.remove(upgrade);
    setCurrentCharacteristics();
  }

  public Characteristic getCharacteristic() { return characteristic; }
}