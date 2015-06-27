package com.ThirtyNineEighty.Game.Objects;

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

  private static EngineObjectDescription initializer;
  static
  {
    initializer = new EngineObjectDescription();
    initializer.VisualModels = new EngineObjectDescription.VisualModel[landsCount];

    for (int i = 0; i < initializer.VisualModels.length; i++)
      initializer.VisualModels[i] = new EngineObjectDescription.VisualModel("land", "land");
  }

  public Land()
  {
    super(initializer);
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
