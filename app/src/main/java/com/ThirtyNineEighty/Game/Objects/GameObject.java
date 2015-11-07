package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;

public abstract class GameObject<TDescription extends GameDescription, TProperties extends GameProperties>
  extends WorldObject<TDescription, TProperties>
{
  private static final long serialVersionUID = 1L;

  protected float health;
  protected boolean isDead;

  protected GameObject(String type, TProperties properties)
  {
    super(type, properties);

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
}