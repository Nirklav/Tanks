package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Common.Math.Angle;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.Renderable.Light;

public class TankSelectWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  private float angle = 35;
  private float length = 8;

  private GameStartArgs args;

  public TankSelectWorld(GameStartArgs args)
  {
    this.args = args;
  }

  @Override
  public void initialize()
  {
    add(new Land());
    add(player = new Tank(args.getTankName()));
    super.initialize();
  }

  public void addLength(float value)
  {
    length += value;

    if (length < 4)
      length = 4;

    if (length > 10)
      length = 10;
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

  @Override
  public void setCamera(Camera camera)
  {
    camera.target.setFrom(player.getPosition());

    float height = length / 2;
    float x = length * (float)Math.cos(Math.toRadians(angle));
    float y = length * (float)Math.sin(Math.toRadians(angle));

    camera.eye.setFrom(x, y, height);
  }

  @Override
  public void setLight(Light light)
  {
    float height = 10 + length / 2;
    float x = length * (float)Math.cos(Math.toRadians(angle));
    float y = length * (float)Math.sin(Math.toRadians(angle));

    light.Position.setFrom(x, y, height);
  }
}
