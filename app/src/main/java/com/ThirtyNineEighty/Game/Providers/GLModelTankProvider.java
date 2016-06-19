package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLModelWorldObjectProvider;
import com.ThirtyNineEighty.Base.Providers.GLRenderableWorldObjectProvider;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.Base.Renderable.GL.GLParticles;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Base.Renderable.GL.GLModel;

public class GLModelTankProvider
  extends GLModelWorldObjectProvider<Tank>
{
  private static final long serialVersionUID = 1L;

  private boolean destroyed;

  public GLModelTankProvider(Tank tank, VisualDescription description)
  {
    super(tank, description);
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    super.set(data, description);

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
    case Tank.EventHit: blowup(); break;
    }
  }

  private void blowup()
  {
    if (destroyed)
      return;
    if (object.getHealth() > 0)
      return;

    destroyed = true;
    IDataProvider<GLExplosionParticles.Data> provider;

    provider = new GLRenderableWorldObjectProvider<>(object, GLExplosionParticles.Data.class, null);
    GLExplosionParticles explosion = new GLExplosionParticles(provider)
      .setLife(1000)
      .setPointSize(30, 60)
      .setCount(2000);

    provider = new GLRenderableWorldObjectProvider<>(object, GLExplosionParticles.Data.class, null);
    GLParticles fire = new GLParticles(provider)
      .setPointSize(30, 70)
      .setUpdateTimeout(10)
      .setCount(400)
      .setSize(9)
      .setMaxTime(20000)
      .setAngleVariance(30)
      .setColor(new Vector3(1.4f, 0.6f, 0.0f, 1.0f), new Vector3(0.3f, 0.3f, 0.3f, 0.0f));

    object.bind(explosion);
    object.bind(fire);
  }
}
