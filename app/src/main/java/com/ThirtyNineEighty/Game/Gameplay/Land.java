package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.EngineObjectDescription;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Spiral;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

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
    IContent content = GameContext.getContent();
    IWorld world = content.getWorld();
    IEngineObject player = world.getPlayer();

    Vector3 position = player.getPosition();

    Vector2 shift = Spiral.get(index);
    shift.multiplyToX(landSize);
    shift.multiplyToY(landSize);
    shift.addToX(landSize * Math.signum(position.getX()) / 2);
    shift.addToY(landSize * Math.signum(position.getY()) / 2);

    int landsCountX = (int)(position.getX() / landSize);
    int landsCountY = (int)(position.getY() / landSize);

    Vector3 renderPos = Vector.getInstance(3);
    renderPos.setX(landsCountX * landSize + shift.getX());
    renderPos.setY(landsCountY * landSize + shift.getY());

    renderable.setGlobal(renderPos, angles);
    
    Vector.release(renderPos);
    Vector.release(shift);
  }
}
