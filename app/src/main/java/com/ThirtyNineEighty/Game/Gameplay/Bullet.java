package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

public class Bullet extends GameObject
{
  protected Bullet(String type)
  {
    super(CharacteristicFactory.get(type));

    bindProgram(new MoveSubprogram(this, 100));
  }

  @Override
  public void onCollide(IEngineObject object)
  {
    super.onCollide(object);

    IContent content = GameContext.getContent();
    IWorld world = content.getWorld();
    world.remove(this);

    if (!(object instanceof GameObject))
      return;

    GameObject target = (GameObject)object;
    Characteristic targetCharacteristic = target.getCharacteristics();
    Characteristic bulletCharacteristic = getCharacteristics();

    targetCharacteristic.addHealth(-bulletCharacteristic.getDamage());

    //if (targetCharacteristic.getHealth() <= 0)
    //  world.remove(object);
  }
}
