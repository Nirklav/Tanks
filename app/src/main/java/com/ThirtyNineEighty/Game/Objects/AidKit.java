package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Resources.Sources.FileCharacteristicSource;
import com.ThirtyNineEighty.System.GameContext;

public class AidKit
  extends GameObject
{
  protected AidKit(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileCharacteristicSource(type)));
  }

  @Override
  public void collide(EngineObject object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject gameObject = (GameObject) object;

    Characteristic objectCh = gameObject.getCharacteristic();
    Characteristic aidKitCh = getCharacteristic();

    objectCh.addHealth(aidKitCh.getHealth());
  }
}
