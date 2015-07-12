package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Resources.Sources.FileCharacteristicSource;
import com.ThirtyNineEighty.System.GameContext;

public class Bullet extends GameObject
{
  protected Bullet(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileCharacteristicSource(type)));
  }

  @Override
  public void initialize()
  {
    super.initialize();
    bindProgram(new MoveSubprogram(this, 100));
  }

  @Override
  public void collide(EngineObject object)
  {
    if (!(object instanceof GameObject))
      return;

    IWorld world = GameContext.content.getWorld();

    GameObject target = (GameObject) object;
    Characteristic targetCharacteristic = target.getCharacteristic();
    Characteristic bulletCharacteristic = getCharacteristic();

    targetCharacteristic.addHealth(-bulletCharacteristic.getDamage());

    if (targetCharacteristic.getHealth() <= 0)
      world.remove(object);
  }
}
