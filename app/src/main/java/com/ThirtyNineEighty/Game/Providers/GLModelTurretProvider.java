package com.ThirtyNineEighty.Game.Providers;

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
      .setLife(400)
      .setCount(1000)
      .setAngleVariance(90)
      .setPointSize(30, 45)
      .setExplosionSize(6);

    object.bind(particles);
  }
}
