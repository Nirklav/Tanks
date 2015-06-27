package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

public class TurnSubprogram extends Subprogram
{
  protected GameObject movedObject;
  protected float coefficient;

  public TurnSubprogram(GameObject obj, int coeff)
  {
    super();

    movedObject = obj;
    coefficient = Math.signum(coeff);
  }

  @Override
  public void onUpdate()
  {
    Characteristic c = movedObject.getCharacteristics();

    Vector3 angles = Vector.getInstance(3);
    angles.addToZ(coefficient * c.getRotationSpeed() * GameContext.getDelta());

    GameContext.collisions.rotate(movedObject, angles);

    Vector.release(angles);
  }
}
