package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.Collision;
import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Resources.Sources.FileCharacteristicSource;
import com.ThirtyNineEighty.System.GameContext;

public class Bullet extends GameObject
{
  protected Bullet(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileCharacteristicSource(type)));

    bindProgram(new MoveSubprogram(this, 100));
  }

  @Override
  public void collide(EngineObject object, Collision<Vector3> collision)
  {
    IWorld world = GameContext.content.getWorld();
    world.remove(this);

    if (!(object instanceof GameObject))
      return;

    GameObject target = (GameObject) object;
    Characteristic targetCharacteristic = target.getCharacteristic();
    Characteristic bulletCharacteristic = getCharacteristic();

    targetCharacteristic.addHealth(-bulletCharacteristic.getDamage());

    if (targetCharacteristic.getHealth() <= 0)
      world.remove(object);
  }
}
