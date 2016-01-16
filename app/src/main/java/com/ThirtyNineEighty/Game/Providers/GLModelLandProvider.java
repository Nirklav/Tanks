package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.DataProvider;
import com.ThirtyNineEighty.Base.Providers.RenderableDataProvider;
import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLModel;
import com.ThirtyNineEighty.Game.TanksContext;

public class GLModelLandProvider
  extends RenderableDataProvider<GLModel.Data>
{
  private static final long serialVersionUID = 1L;

  private final Land land;
  private final VisualDescription description;

  public GLModelLandProvider(Land land, VisualDescription description)
  {
    super(GLModel.Data.class);
    this.description = description;
    this.land = land;
  }

  @Override
  public void set(GLModel.Data data)
  {
    super.set(data);

    data.position.setFrom(getPosition(description.id));
    data.angles.setFrom(land.getAngles());
    data.scale = 1;
  }

  private Vector3 getPosition(int id)
  {
    IWorld world = TanksContext.content.getWorld();
    WorldObject<?, ?> player = world.getPlayer();

    Vector3 position = player != null
      ? player.getPosition()
      : land.getPosition();

    Vector2 shift = Spiral.get(id);
    shift.multiplyToX(Land.size);
    shift.multiplyToY(Land.size);
    shift.addToX(Land.size * Math.signum(position.getX()) / 2);
    shift.addToY(Land.size * Math.signum(position.getY()) / 2);

    int landNumX = (int) (position.getX() / Land.size);
    int landNumY = (int) (position.getY() / Land.size);

    Vector3 renderPos = Vector.getInstance(3);
    renderPos.setX(landNumX * Land.size + shift.getX());
    renderPos.setY(landNumY * Land.size + shift.getY());
    renderPos.setZ(-0.1f);

    Vector.release(shift);

    return renderPos;
  }
}
