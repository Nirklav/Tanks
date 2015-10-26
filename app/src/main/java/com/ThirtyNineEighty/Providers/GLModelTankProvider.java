package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Renderable.GL.GLModel;

public class GLModelTankProvider
  extends DataProvider<GLModel.Data, VisualDescription>
{
  private final Tank tank;

  public GLModelTankProvider(Tank tank, VisualDescription description)
  {
    super(new GLModel.Data(), description);
    this.tank = tank;
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    data.position.setFrom(tank.getPosition());
    data.angles.setFrom(tank.getAngles());

    data.localPosition = Vector3.zero;

    switch (description.id)
    {
    case 0: // chassis
      data.localAngles = Vector3.zero;
      break;
    case 1: // turret
      data.localAngles.setFrom(0, 0, tank.getRelativeTurretAngle());
      break;
    }

    data.scale = 1;

    if (tank.getHealth() <= 0)
    {
      data.RedCoeff = 0.2f;
      data.GreenCoeff = 0.2f;
      data.BlueCoeff = 0.2f;
    }
  }
}
