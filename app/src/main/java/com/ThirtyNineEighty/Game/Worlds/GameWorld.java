package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Renderable.Common.Camera;
import com.ThirtyNineEighty.Renderable.Common.Light;

public class GameWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  private static final float lightHeight = 100;
  private static final float lightX = 50;
  private static final float lightY = 50;

  public GameWorld(final Tank player)
  {
    this.player = player;
    add(player);

    bind(new Camera(new Camera.Setter()
    {
      @Override
      public void set(Camera.Data camera)
      {
        camera.target.setFrom(player.getPosition());
        camera.eye.setFrom(player.getPosition());

        camera.eye.addToY(14);
        camera.eye.addToZ(35);
      }
    }));

    bind(new Light(new Light.Setter()
    {
      @Override
      public void set(Light.Data light)
      {
        light.Position.setFrom(lightX, lightY, lightHeight);
      }
    }));
  }

  @Override
  public boolean needSave() { return true; }
}
