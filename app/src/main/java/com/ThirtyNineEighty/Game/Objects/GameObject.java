package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Renderable.GL.GLParticles;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

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

  public void addHealth(float value)
  {
    health += value;
    checkHealth();
  }

  public void subtractHealth(float value)
  {
    health -= value;
    checkHealth();
  }

  private void checkHealth()
  {
    if (health > 0)
      return;

    final GameObject current = this;
    final GLParticles particles = new GLParticles(GLParticles.Hemisphere, 2, 1000, new LocationProvider(this));

    addRenderable(particles);

    bindProgram(new Subprogram()
    {
      boolean delay = true;

      @Override
      protected void onUpdate()
      {
        if (delay)
        {
          delay(2000);
          delay = false;
          return;
        }
        current.removeRenderable(particles);
        unbind();
      }
    });
  }
}