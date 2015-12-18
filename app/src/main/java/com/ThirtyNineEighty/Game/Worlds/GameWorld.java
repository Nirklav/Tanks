package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Providers.DataProvider;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.Common.CameraView;
import com.ThirtyNineEighty.Renderable.Common.LightView;
import com.ThirtyNineEighty.Renderable.Light;

public class GameWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  public GameWorld(final Tank player)
  {
    this.player = player;
    add(player);

    bind(new CameraView(new DataProvider<Camera>(Camera.class)
    {
      @Override
      public void set(Camera camera)
      {
        camera.target.setFrom(player.getPosition());
        camera.eye.setFrom(player.getPosition());

        camera.eye.addToY(14);
        camera.eye.addToZ(35);
      }
    }));

    bind(new LightView(new DataProvider<Light>(Light.class)
    {
      @Override
      public void set(Light light)
      {
        light.Position.setFrom(50, 50, 200);
      }
    }));
  }

  @Override
  public boolean needSave() { return true; }
}
