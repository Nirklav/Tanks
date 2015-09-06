package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.System.State;

public abstract class GameObject
  extends WorldObject
{
  private float health;
  private boolean isDead;

  public GameObject(State s)
  {
    super(s);

    GameState state = (GameState) s;
    health = state.health;
    isDead = health <= 0;
  }

  protected GameObject(String name, String type, GameProperties properties)
  {
    super(name, type, properties);

    GameDescription description = getDescription();
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
    if (health > 0 || isDead)
      return;

    isDead = true;
    bind(new GLExplosionParticles(GLExplosionParticles.Hemisphere, 1000, 2000, new LocationProvider(this)));
  }

  @Override
  protected State createState()
  {
    return new GameState();
  }

  @Override
  public State getState()
  {
    GameState state = (GameState)super.getState();
    state.health = health;
    return state;
  }

  protected static class GameState
    extends ObjectState
  {
    private static final long serialVersionUID = 1L;

    protected float health;
  }
}