package com.ThirtyNineEighty.Game.Menu.Controls;

public class ToggleButton
  extends Button
{
  public ToggleButton(String caption)
  {
    super(caption);
  }

  @Override
  public void onDown(float x, float y)
  {
    sprite.setImage(state ? notPressed: pressed);
  }

  @Override
  public void onUp(float x, float y)
  {
    state = !state;
    sprite.setImage(state ? pressed : notPressed);
  }
}
