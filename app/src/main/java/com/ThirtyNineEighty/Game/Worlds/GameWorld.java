package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.System.GameContext;

public class GameWorld
  extends BaseWorld
{
  private GameStartArgs args;

  public GameWorld(GameStartArgs args)
  {
    this.args = args;
  }

  @Override
  public void initialize()
  {
    player = GameContext.mapManager.load(args);
    super.initialize();
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
