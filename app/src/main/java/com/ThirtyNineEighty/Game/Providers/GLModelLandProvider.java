package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.*;
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

  public GLModelLandProvider(Land land)
  {
    super(GLModel.Data.class);
    this.land = land;
  }

  @Override
  public void set(GLModel.Data data)
  {
    super.set(data);

    data.position.setFrom(getPosition());
    data.angles.setFrom(land.getAngles());
    data.scale = 1;
  }

  private Vector3 getPosition()
  {
    IWorld world = TanksContext.content.getWorld();
    WorldObject<?, ?> player = world.getPlayer();

    Vector3 position = player != null
      ? player.getPosition()
      : land.getPosition();

    int landNumX = (int) (position.getX() / Land.size);
    int landNumY = (int) (position.getY() / Land.size);

    Vector3 renderPos = Vector3.getInstance();
    renderPos.setX(landNumX * Land.size + Land.size * Math.signum(position.getX()) / 2);
    renderPos.setY(landNumY * Land.size + Land.size * Math.signum(position.getY()) / 2);
    renderPos.setZ(-0.1f);

    return renderPos;
  }
}
