package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLModelWorldObjectProvider;
import com.ThirtyNineEighty.Base.Providers.GLRenderableWorldObjectProvider;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.Base.Renderable.GL.GLRenderable;
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

    if (!destroyed && object.getHealth() <= 0)
    {
      destroyed = true;

      IDataProvider<GLRenderable.Data> provider = new GLRenderableWorldObjectProvider<>(object, GLRenderable.Data.class, null);
      object.bind(new GLExplosionParticles(1000, 2000, 120, provider));
    }
  }
}
