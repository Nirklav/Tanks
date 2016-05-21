package com.ThirtyNineEighty.Base.Providers;

import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Renderable.GL.GLRenderable;

public class GLRenderableWorldObjectProvider<TData extends GLRenderable.Data, TObject extends WorldObject<?, ?>>
  extends RenderableDataProvider<TData>
{
  private static final long serialVersionUID = 1L;

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
    super.set(data);

    data.position.setFrom(object.getPosition());
    data.angles.setFrom(object.getAngles());
    data.scale = 1;

    set(data, description);
  }

  protected void set(TData data, VisualDescription description) { }
}
