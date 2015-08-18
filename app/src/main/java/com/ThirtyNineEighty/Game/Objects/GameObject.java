package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Objects.States.GameState;
import com.ThirtyNineEighty.Game.Objects.States.State;
import com.ThirtyNineEighty.Game.Subprograms.TimedSubprogram;
import com.ThirtyNineEighty.Renderable.GL.GLExplosionParticles;

public abstract class GameObject
  extends WorldObject
{
  private float health;
  private boolean isDead;

  protected GameObject(GameState state)
  {
    super(state);

    health = state.getHealth();
    isDead = health <= 0;
  }

  protected GameObject(String type, GameProperties properties)
  {
    super(type, properties);

    GameDescription description = getDescription();
    health = description.getHealth();
  }

  public GameDescription getDescription() { return (GameDescription) description; }
  public GameProperties getProperties() { return (GameProperties) properties; }

  @Override
  protected State createState()
  {
    return new GameState();
  }

  @Override
  public State getState()
  {
    GameState state = (GameState)super.getState();
    state.setHealth(health);
    return state;
  }

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
    if (health > 0 || isDead)
      return;

    isDead = true;

    GLExplosionParticles particles = new GLExplosionParticles(GLExplosionParticles.Hemisphere, 1.0f, 2000, new LocationProvider(this));
    bind(new TimedSubprogram(particles, 1100));
  }
}