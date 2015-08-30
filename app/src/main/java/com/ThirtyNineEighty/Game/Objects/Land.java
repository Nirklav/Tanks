package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.System.*;

public class Land
  extends WorldObject
{
  private static final float landSize = 50.0f;

  public Land()
  {
    super("land", new Properties());
  }

  public Land(State state)
  {
    super(state);
  }

  @Override
  protected ILocationProvider<Vector3> createPositionProvider(VisualDescription visual)
  {
    return new LocationProvider(this, visual);
  }

  private static class LocationProvider
    implements ILocationProvider<Vector3>
  {
    private WorldObject source;
    private int id;

    public LocationProvider(WorldObject source, VisualDescription visual)
    {
      this.source = source;
      this.id = visual.id;
    }

    @Override
    public Location<Vector3> getLocation()
    {
      Location<Vector3> location = new Location<>(3);
      location.position.setFrom(getPosition());
      location.angles.setFrom(source.angles);
      return location;
    }

    public Vector3 getPosition()
    {
      IWorld world = GameContext.content.getWorld();
      WorldObject player = world.getPlayer();

      Vector3 position = player != null
        ? player.getPosition()
        : Vector3.zero;

      Vector2 shift = Spiral.get(id);
      shift.multiplyToX(landSize);
      shift.multiplyToY(landSize);
      shift.addToX(landSize * Math.signum(position.getX()) / 2);
      shift.addToY(landSize * Math.signum(position.getY()) / 2);

      int landNumX = (int) (position.getX() / landSize);
      int landNumY = (int) (position.getY() / landSize);

      Vector3 renderPos = Vector.getInstance(3);
      renderPos.setX(landNumX * landSize + shift.getX());
      renderPos.setY(landNumY * landSize + shift.getY());
      renderPos.setZ(-0.1f);

      Vector.release(shift);

      return renderPos;
    }
  }
}
