package com.ThirtyNineEighty.Base.Providers;

import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Renderable.GL.GLModel;

public class GLModelWorldObjectProvider<TObject extends WorldObject<?, ?>>
  extends GLRenderableWorldObjectProvider<GLModel.Data, TObject>
{
  private static final long serialVersionUID = 1L;

  public GLModelWorldObjectProvider(TObject object, VisualDescription description)
  {
    super(object, GLModel.Data.class, description);
  }
}
