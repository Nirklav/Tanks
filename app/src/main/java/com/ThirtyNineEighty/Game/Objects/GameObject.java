package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;

public abstract class GameObject
  extends EngineObject
{
  private float health;

  protected GameObject(GameDescription description, GameProperties properties)
  {
    super(description, properties);

    health = description.getHealth();
  }

  public GameDescription getDescription() { return (GameDescription) description; }
  public GameProperties getProperties() { return (GameProperties) properties; }

  public float getHealth() { return health; }
  public void addHealth(float value) { health += value; }
  public void subtractHealth(float value) { health -= value; }
}