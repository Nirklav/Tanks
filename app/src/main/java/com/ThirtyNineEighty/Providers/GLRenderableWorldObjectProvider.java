package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.GL.GLRenderable;

public class GLRenderableWorldObjectProvider
  extends DataProvider<GLRenderable.Data, VisualDescription>
{
  private final WorldObject object;

  public GLRenderableWorldObjectProvider(WorldObject object, VisualDescription description)
  {
    super(new GLRenderable.Data(), description);
    this.object = object;
  }

  @Override
  public void set(GLRenderable.Data data, VisualDescription description)
  {
    data.position.setFrom(object.getPosition());
    data.angles.setFrom(object.getAngles());

    data.localPosition = Vector3.zero;
    data.localAngles = Vector3.zero;

    data.scale = 1;
  }
}
