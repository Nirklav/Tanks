package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLModelWorldObjectProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLModel;
import com.ThirtyNineEighty.Game.Objects.Tank;

public class GLModelTankTurretProvider
  extends GLModelWorldObjectProvider<Tank>
{
  private static final long serialVersionUID = 1L;

  public GLModelTankTurretProvider(Tank tank, VisualDescription description)
  {
    super(tank, description);
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    super.set(data, description);

    data.localAngles.setFrom(0, 0, object.getRelativeTurretAngle());

    if (object.getHealth() <= 0)
    {
      data.redCoeff = 0.2f;
      data.greenCoeff = 0.2f;
      data.blueCoeff = 0.2f;
    }
  }
}
