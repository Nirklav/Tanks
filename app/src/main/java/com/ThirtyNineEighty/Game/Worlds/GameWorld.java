package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.System.GameContext;

public class GameWorld
  extends BaseWorld
{
  @Override
  public void initialize(Object obj)
  {
    if (!(obj instanceof GameStartArgs))
      throw new IllegalArgumentException("Illegal args type");

    GameStartArgs args = (GameStartArgs) obj;
    player = GameContext.mapManager.load(args);

    GameContext.content.setMenu(new GameMenu());
    super.initialize(args);
  }

  @Override
  public void setCamera(Camera camera)
  {
    camera.target.setFrom(player.getPosition());
    camera.eye.setFrom(player.getPosition());

    camera.eye.addToY(14);
    camera.eye.addToZ(35);
  }

  @Override
  public void setLight(Vector3 lightPosition)
  {
    lightPosition.setFrom(player.getPosition());
    lightPosition.setZ(30);
  }
}
