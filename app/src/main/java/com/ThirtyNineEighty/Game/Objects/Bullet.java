package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.System.State;

public class Bullet
  extends GameObject
{
  public Bullet(State state)
  {
    super(state);
  }

  public Bullet(String type)
  {
    super(type, new GameProperties());
  }

  @Override
  public void initialize()
  {
    bind(new MoveSubprogram(this, 100));

    super.initialize();
  }

  @Override
  public void collide(WorldObject object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject target = (GameObject) object;
    GameDescription bulletDescription = getDescription();

    target.subtractHealth(bulletDescription.getDamage());
  }
}
