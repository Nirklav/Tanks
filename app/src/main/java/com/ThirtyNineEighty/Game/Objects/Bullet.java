package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;

public class Bullet extends GameObject
{
  protected Bullet(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileDescriptionSource(type)), new GameProperties());
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
    GameDescription bulletDescription = getDescription();

    target.subtractHealth(bulletDescription.getDamage());
    if (target.getHealth() <= 0)
      world.remove(object);
  }
}
