package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.System.State;

public class AidKit
  extends GameObject
{
  public AidKit(State state)
  {
    super(state);
  }

  public AidKit(String type)
  {
    super(null, type, new GameProperties());
  }

  @Override
  public void collide(WorldObject object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject gameObject = (GameObject) object;
    GameDescription aidKitDescription = getDescription();

    gameObject.addHealth(aidKitDescription.getHealth());
  }
}
