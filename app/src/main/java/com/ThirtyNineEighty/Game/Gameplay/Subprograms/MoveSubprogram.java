package com.ThirtyNineEighty.Game.Gameplay.Subprograms;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.System.GameContext;

public class MoveSubprogram
  extends GameSubprogram
{
  protected GameObject movedObject;
  protected float length;

  public MoveSubprogram(GameObject obj) { this (obj, 0); }
  public MoveSubprogram(GameObject obj, float len)
  {
    super();

    movedObject = obj;
    length = len;
  }

  @Override
  public void onUpdate()
  {
    Characteristic c = movedObject.getCharacteristics();

    float stepLength = c.getSpeed() * GameContext.getDelta();
    world.collisionManager.move(movedObject, stepLength);

    length -= stepLength;
    if (length < 0)
      world.remove(movedObject);
  }
}
