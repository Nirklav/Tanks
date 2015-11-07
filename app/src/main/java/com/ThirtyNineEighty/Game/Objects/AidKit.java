package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;

public class AidKit
  extends GameObject<GameDescription, GameProperties>
{
  private static final long serialVersionUID = 1L;

  public AidKit(String type)
  {
    super(type, new GameProperties());
  }

  @Override
  public void collide(WorldObject<?, ?> object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject<?, ?> gameObject = (GameObject<?, ?>) object;
    GameDescription aidKitDescription = getDescription();

    gameObject.addHealth(aidKitDescription.getHealth());
  }
}
