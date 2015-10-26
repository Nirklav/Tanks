package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Renderable.GL.GLModel;

public class GLModelWorldObjectProvider
  extends DataProvider<GLModel.Data, VisualDescription>
{
  private final WorldObject object;

  public GLModelWorldObjectProvider(WorldObject object, VisualDescription description)
  {
    super(new GLModel.Data(), description);
    this.object = object;
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    data.position.setFrom(object.getPosition());
    data.angles.setFrom(object.getAngles());

    data.localPosition = Vector3.zero;
    data.localAngles = Vector3.zero;

    data.scale = 1;
  }
}