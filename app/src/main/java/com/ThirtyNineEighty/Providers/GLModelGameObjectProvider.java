package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Renderable.GL.*;

public class GLModelGameObjectProvider<TObject extends GameObject<?, ?>>
  extends GLModelWorldObjectProvider<TObject>
{
  private boolean destroyed;

  public GLModelGameObjectProvider(TObject object, VisualDescription description)
  {
    super(object, description);
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    super.set(data, description);

    if (!destroyed && object.getHealth() <= 0)
    {
      destroyed = true;

      IDataProvider<GLRenderable.Data> provider = new GLRenderableWorldObjectProvider<>(object, new GLRenderable.Data(), null);
      object.bind(new GLExplosionParticles(1000, 2000, GLExplosionParticles.Hemisphere, provider));
    }
  }
}
