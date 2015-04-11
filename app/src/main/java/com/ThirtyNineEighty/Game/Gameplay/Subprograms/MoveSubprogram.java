package com.ThirtyNineEighty.Game.Gameplay.Subprograms;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.System.GameContext;

public class MoveSubprogram
  extends GameSubprogram
{
  protected GameObject movedObject;

  public MoveSubprogram(GameObject obj)
  {
    super();

    movedObject = obj;
  }

  @Override
  public void onUpdate()
  {
    Characteristic c = movedObject.getCharacteristics();
    world.collisionManager.move(movedObject, c.getSpeed() * GameContext.getDelta());
  }

  @Override
  protected void onLifeTimeEnd()
  {
    world.remove(movedObject);
  }
}
