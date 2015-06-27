package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.Collision;
import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector3;
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
  public void collide(EngineObject object, Collision<Vector3> collision)
  {
    IWorld world = GameContext.content.getWorld();
    world.remove(this);

    if (!(object instanceof GameObject))
      return;

    GameObject gameObject = (GameObject) object;

    Characteristic objectCh = gameObject.getCharacteristic();
    Characteristic aidKitCh = getCharacteristic();

    objectCh.addHealth(aidKitCh.getHealth());
  }
}
