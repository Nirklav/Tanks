package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Land;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Helpers.Angle;
import com.ThirtyNineEighty.System.Camera;

public class TankSelectWorld
  extends BaseWorld
{
  private float angle = 35;
  private float length = 8;

  @Override
  public void initialize(Object args)
  {
    add(new Land());
    add(player = new Tank(CharacteristicFactory.Tank));

    super.initialize(args);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();
  }

  public void addLength(float value)
  {
    length += value;

    if (length < 2)
      length = 2;

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

    float height = 3 + length / 4;
    float x = length * (float)Math.cos(Math.toRadians(angle));
    float y = length * (float)Math.sin(Math.toRadians(angle));

    camera.eye.setFrom(x, y, height);
  }
}
