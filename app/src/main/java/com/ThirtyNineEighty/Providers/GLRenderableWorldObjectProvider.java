package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.GL.GLRenderable;

public class GLRenderableWorldObjectProvider<TData extends GLRenderable.Data, TObject extends WorldObject<?, ?>>
  extends DataProvider<TData, VisualDescription>
{
  protected final TObject object;

  public GLRenderableWorldObjectProvider(TObject object, TData data, VisualDescription description)
  {
    super(data, description);
    this.object = object;
  }

  @Override
  public void set(TData data, VisualDescription description)
  {
    data.position.setFrom(object.getPosition());
    data.angles.setFrom(object.getAngles());
    data.scale = 1;
  }
}
