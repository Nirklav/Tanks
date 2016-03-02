package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;

public class AidKit
  extends GameObject<GameDescription, GameProperties>
{
  private static final long serialVersionUID = 1L;

  public AidKit(String descriptionName)
  {
    super(descriptionName, new GameProperties());
  }

  @Override
  public void collide(WorldObject<?, ?> object)
  {
    if (object instanceof GameObject)
    {
      GameObject<?, ?> gameObject = (GameObject<?, ?>) object;
      gameObject.addHealth(description.getHealth());
    }
  }
}
