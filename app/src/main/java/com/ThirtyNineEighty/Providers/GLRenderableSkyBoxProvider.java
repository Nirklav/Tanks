package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.SkyBoxProperties;
import com.ThirtyNineEighty.Game.Objects.SkyBox;
import com.ThirtyNineEighty.Renderable.GL.GLRenderable;

public class GLRenderableSkyBoxProvider
  extends GLRenderableWorldObjectProvider<GLRenderable.Data, SkyBox>
{
  public GLRenderableSkyBoxProvider(SkyBox object, VisualDescription description)
  {
    super(object, new GLRenderable.Data(), description);
  }

  @Override
  public void set(GLRenderable.Data data, VisualDescription description)
  {
    super.set(data, description);

    SkyBoxProperties properties = object.getProperties();
    data.scale = properties.getScale();
  }
}
