package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.Properties.SkyBoxProperties;
import com.ThirtyNineEighty.Game.Objects.SkyBox;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Common.Math.Angle;
import com.ThirtyNineEighty.Providers.DataProvider;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.Common.CameraView;
import com.ThirtyNineEighty.Renderable.Common.LightView;
import com.ThirtyNineEighty.Renderable.Light;

public class TankSelectWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  private static final float skyBoxScale = 100;
  private static final float minLength = 4;
  private static final float maxLength = 10;
  private static final float lightHeight = 100;
  private static final float lightX = 50;
  private static final float lightY = 50;

  private float angle = 35;
  private float length = 8;

  public TankSelectWorld(GameStartArgs args)
  {
    add(new Land(Land.sand));
    add(new SkyBox(SkyBox.desert, new SkyBoxProperties(skyBoxScale)));
    add(player = new Tank(args.getTankName()));

    bind(new CameraView(new DataProvider<Camera>(Camera.class)
    {
      @Override
      public void set(Camera camera)
      {
        camera.target.setFrom(player.getPosition());

        float height = length / 2;
        float x = length * (float)Math.cos(Math.toRadians(angle));
        float y = length * (float)Math.sin(Math.toRadians(angle));

        camera.eye.setFrom(x, y, height);
      }
    }));

    bind(new LightView(new DataProvider<Light>(Light.class)
    {
      @Override
      public void set(Light light)
      {
        light.Position.setFrom(lightX, lightY, lightHeight);
      }
    }));
  }

  public void addLength(float value)
  {
    length += value;

    if (length < minLength)
      length = minLength;

    if (length > maxLength)
      length = maxLength;
  }

  public void addAngle(float value)
  {
    angle = Angle.correct(angle + value);
  }

  public void setPlayer(String tankName)
  {
    remove(player);
    add(player = new Tank(tankName));
  }
}
