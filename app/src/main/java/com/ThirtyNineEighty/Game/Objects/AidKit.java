package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;

public class AidKit
  extends GameObject
{
  protected AidKit(String type)
  {
    super(GameContext.resources.getCharacteristic(new FileDescriptionSource(type)), new GameProperties());
  }

  @Override
  public void collide(EngineObject object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject gameObject = (GameObject) object;
    GameDescription aidKitDescription = getDescription();

    gameObject.addHealth(aidKitDescription.getHealth());
  }
}
