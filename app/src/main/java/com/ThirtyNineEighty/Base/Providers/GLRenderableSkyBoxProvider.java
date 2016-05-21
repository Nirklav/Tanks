package com.ThirtyNineEighty.Base.Providers;

import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Objects.Properties.SkyBoxProperties;
import com.ThirtyNineEighty.Base.Objects.SkyBox;
import com.ThirtyNineEighty.Base.Renderable.GL.GLRenderable;

public class GLRenderableSkyBoxProvider
  extends GLRenderableWorldObjectProvider<GLRenderable.Data, SkyBox>
{
  private static final long serialVersionUID = 1L;

  public GLRenderableSkyBoxProvider(SkyBox object, VisualDescription description)
  {
    super(object, GLRenderable.Data.class, description);
  }

  @Override
  public void set(GLRenderable.Data data, VisualDescription description)
  {
    super.set(data, description);

    SkyBoxProperties properties = object.getProperties();
    data.scale = properties.getScale();
  }
}
