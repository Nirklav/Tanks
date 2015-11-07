package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Common.Math.Spiral;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.GL.GLModel;
import com.ThirtyNineEighty.System.GameContext;

public class GLModelLandProvider
  extends DataProvider<GLModel.Data, VisualDescription>
{
  private static final long serialVersionUID = 1L;

  private final Land land;

  public GLModelLandProvider(Land land, VisualDescription description)
  {
    super(new GLModel.Data(), description);
    this.land = land;
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    data.position.setFrom(getPosition(description.id));
    data.angles.setFrom(land.getAngles());
    data.scale = 1;
  }

  private Vector3 getPosition(int id)
  {
    IWorld world = GameContext.content.getWorld();
    WorldObject<?, ?> player = world.getPlayer();

    Vector3 position = player != null
      ? player.getPosition()
      : Vector3.zero;

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
