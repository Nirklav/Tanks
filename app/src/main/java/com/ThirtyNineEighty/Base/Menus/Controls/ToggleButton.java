package com.ThirtyNineEighty.Base.Menus.Controls;

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
    sprite.setImage(state ? notPressedImageName : pressedImageName);
  }

  @Override
  public void onUp(float x, float y)
  {
    state = !state;
    sprite.setImage(state ? pressedImageName : notPressedImageName);
  }
}
