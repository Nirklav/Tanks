package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.Collision;
import com.ThirtyNineEighty.Game.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;

public class Bullet extends GameObject
{
  protected Bullet(String type)
  {
    super(CharacteristicFactory.get(type));

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
    Characteristic targetCharacteristic = target.getCharacteristics();
    Characteristic bulletCharacteristic = getCharacteristics();

    targetCharacteristic.addHealth(-bulletCharacteristic.getDamage());

    if (targetCharacteristic.getHealth() <= 0)
      world.remove(object);
  }
}
