package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Spiral;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.System.GameContext;

public class Land
  extends EngineObject
{
  private static final int landsCount = 9;
  private static final float landSize = 50.0f;

  private static Description description;
  private static Properties properties;
  static
  {
    VisualDescription[] visuals = new VisualDescription[landsCount];

    description = new Description(visuals, null);
    properties = new Properties();

    for (int i = 0; i < visuals.length; i++)
      visuals[i] = new VisualDescription("land", "land", i);
  }

  public Land()
  {
    super(description, properties);
  }

  @Override
  protected ILocationProvider<Vector3> createPositionProvider(VisualDescription visual)
  {
    return new LocationProvider(this, visual);
  }

  private static class LocationProvider
    implements ILocationProvider<Vector3>
  {
    private EngineObject source;
    private int index;

    public LocationProvider(EngineObject source, VisualDescription visual)
    {
      this.source = source;
      this.index = visual.index;
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
      EngineObject player = world.getPlayer();

      Vector3 position = player != null
        ? player.getPosition()
        : Vector3.zero;

      Vector2 shift = Spiral.get(index);
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
