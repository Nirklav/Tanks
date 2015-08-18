package com.ThirtyNineEighty.Game.Objects.States;

public class GameState
  extends State
{
  private static final long serialVersionUID = 1L;

  protected float health;

  public float getHealth() { return health; }
  public void setHealth(float value) { health = value; }
}
