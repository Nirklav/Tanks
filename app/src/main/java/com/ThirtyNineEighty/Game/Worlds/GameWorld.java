package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.Light;

public class GameWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  public GameWorld(Tank player)
  {
    this.player = player;
    add(player);
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
  public void setLight(Light light)
  {
    light.Position.setFrom(50, 50, 200);
  }

  @Override
  public boolean needSave() { return true; }
}
