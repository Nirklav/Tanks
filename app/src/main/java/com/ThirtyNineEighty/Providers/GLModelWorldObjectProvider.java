package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.GL.GLModel;

public class GLModelWorldObjectProvider<TObject extends WorldObject<?, ?>>
  extends GLRenderableWorldObjectProvider<GLModel.Data, TObject>
{
  public GLModelWorldObjectProvider(TObject object, VisualDescription description)
  {
    super(object, new GLModel.Data(), description);
  }
}
