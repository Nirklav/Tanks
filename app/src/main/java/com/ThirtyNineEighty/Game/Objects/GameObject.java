package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;

public abstract class GameObject
  extends EngineObject
{
  private float health;

  protected GameObject(String type, GameProperties properties)
  {
    this(GameContext.resources.getCharacteristic(new FileDescriptionSource(type)), properties);
  }

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