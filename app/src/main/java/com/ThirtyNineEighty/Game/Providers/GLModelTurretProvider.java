package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLModelWorldObjectProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.Base.Renderable.GL.GLModel;
import com.ThirtyNineEighty.Game.Objects.Tank;

public class GLModelTurretProvider
  extends GLModelWorldObjectProvider<Tank>
{
  private static final long serialVersionUID = 1L;

  public GLModelTurretProvider(Tank tank, VisualDescription description)
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

  @Override
  protected void onEvent(String event)
  {
    switch (event)
    {
    case Tank.EventFire: fireEffect(); break;
    }
  }

  private void fireEffect()
  {
    GLExplosionParticles particles = new GLExplosionParticles(new GLModelTurretFireProvider(object))
      .setLifeTime(300)
      .setCount(300)
      .setAngleVariance(30)
      .setColor(new Vector3(0.4f, 0.4f, 0.4f));

    object.bind(particles);
  }
}
