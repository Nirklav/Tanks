package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Spiral;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
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
      visuals[i] = new VisualDescription("land", "land");
  }

  public Land()
  {
    super(description, properties);
  }

  @Override
  protected void setGlobalRenderablePosition(int index, I3DRenderable renderable)
  {
    IWorld world = GameContext.content.getWorld();
    EngineObject player = world.getPlayer();

    Vector3 position = player.getPosition();

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

    renderable.setGlobal(renderPos, angles);

    Vector.release(renderPos);
    Vector.release(shift);
  }
}
