package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Math.Angle;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.System.GameContext;

public abstract class GameObject<TDescription extends GameDescription, TProperties extends GameProperties>
  extends WorldObject<TDescription, TProperties>
{
  private static final long serialVersionUID = 1L;

  protected float health;
  protected boolean isDead;

  protected GameObject(String descriptionName, TProperties properties)
  {
    super(descriptionName, properties);

    health = description.getHealth();
  }

  public float getHealth() { return health; }

  public void addHealth(float value)
  {
    if (isDead)
      return;
    health += value;
  }

  public void subtractHealth(float value)
  {
    health -= value;
    if (health < 0)
      isDead = true;
  }

  public void move()
  {
    move(getSpeed());
  }

  public void moveBack()
  {
    move(- getSpeed());
  }

  private float getSpeed()
  {
    GameDescription description = getDescription();
    return description.getSpeed() * GameContext.getDelta();
  }

  public void rotate(float targetAngleZ)
  {
    GameDescription description = getDescription();

    float correctedAngleZ = Angle.correct(targetAngleZ);
    float speed = description.getRotationSpeed() * GameContext.getDelta();
    float angleZ = angles.getZ();

    if (Math.abs(correctedAngleZ - angleZ) < speed)
      return;

    float addedValue = speed * Angle.getDirection(angleZ, correctedAngleZ);
    rotate(0, 0, addedValue);
  }
}