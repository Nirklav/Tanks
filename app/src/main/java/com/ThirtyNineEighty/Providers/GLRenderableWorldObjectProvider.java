package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.GL.GLRenderable;

public class GLRenderableWorldObjectProvider<TData extends GLRenderable.Data, TObject extends WorldObject<?, ?>>
  extends DataProvider<TData>
{
  protected final TObject object;
  private final VisualDescription description;

  public GLRenderableWorldObjectProvider(TObject object, Class<TData> dataClass, VisualDescription description)
  {
    super(dataClass);

    this.description = description;
    this.object = object;
  }

  @Override
  public final void set(TData data)
  {
    data.position.setFrom(object.getPosition());
    data.angles.setFrom(object.getAngles());
    data.scale = 1;

    set(data, description);
  }

  protected void set(TData data, VisualDescription description)
  {

  }
}
