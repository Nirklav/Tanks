package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;
import com.ThirtyNineEighty.System.ISubprogram;

public class Bullet extends GameObject
{
  private ISubprogram subprogram;

  protected Bullet(String type)
  {
    super(CharacteristicFactory.get(type));

    IContent content = GameContext.getContent();
    content.bindProgram(subprogram = new MoveSubprogram(this).setLifeTime(150));
  }

  @Override
  public void onCollide(IEngineObject object)
  {
    super.onCollide(object);

    if (!(object instanceof GameObject))
      return;

    GameObject target = (GameObject)object;
    Characteristic targetCharacteristic = target.getCharacteristics();
    Characteristic bulletCharacteristic = getCharacteristics();

    targetCharacteristic.addHealth(bulletCharacteristic.getDamage());

    IWorld world = GameContext.getContent().getWorld();
    if (targetCharacteristic.getHealth() <= 0)
      world.remove(object);

    world.remove(this);
  }

  @Override
  public void onRemoved()
  {
    super.onRemoved();

    IContent content = GameContext.getContent();
    content.unbindProgram(subprogram);
  }
}
