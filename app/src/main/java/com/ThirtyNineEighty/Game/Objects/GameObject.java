package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Characteristics.Upgrade;

import java.util.ArrayList;

public abstract class GameObject
  extends EngineObject
{
  private Characteristic baseCharacteristics;
  private Characteristic characteristics;
  private ArrayList<Upgrade> upgrades;

  protected GameObject(Characteristic characteristics)
  {
    super(characteristics.initializer);

    upgrades = new ArrayList<>();

    baseCharacteristics = characteristics;
    setCurrentCharacteristics();
  }

  protected void setCurrentCharacteristics()
  {
    characteristics = new Characteristic(baseCharacteristics);

    for (Upgrade upgrade : upgrades)
      upgrade.doUpgrade(characteristics);
  }

  public void addUpgrade(Upgrade upgrade) { upgrades.add(upgrade); }
  public void removeUpgrade(Upgrade upgrade) { upgrades.remove(upgrade); }

  public Characteristic getCharacteristics() { return characteristics; }
}