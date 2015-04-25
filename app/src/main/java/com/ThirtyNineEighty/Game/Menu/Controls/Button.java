package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;
import com.ThirtyNineEighty.Renderable.Resources.FileImageSource;
import com.ThirtyNineEighty.Renderable.Resources.Image;
import com.ThirtyNineEighty.System.GameContext;

public class Button
  extends Control
{
  private boolean state;

  private GLSprite sprite;

  private Image pressed;
  private Image notPressed;

  private float left;
  private float right;
  private float bottom;
  private float top;

  public Button(float x, float y, float width, float height, String pressedName, String notPressedName)
  {
    left = x - width / 2;
    right = x + width / 2;
    bottom = y - height / 2;
    top = y + height / 2;

    pressed = GameContext.renderableResources.getImage(new FileImageSource(pressedName));
    notPressed = GameContext.renderableResources.getImage(new FileImageSource(notPressedName));

    sprite = new GLSprite(notPressedName);
    sprite.setPosition(x, y);
    sprite.setSize(width, height);
  }

  public void dispose()
  {
    sprite.dispose();
  }

  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();
    sprite.finalize();
  }

  public boolean getState()
  {
    return state;
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    sprite.draw(orthoViewMatrix);
  }

  @Override
  public void onDown(float x, float y)
  {
    state = true;
    sprite.setImage(pressed);
  }

  @Override
  public void onUp(float x, float y)
  {
    state = false;
    sprite.setImage(notPressed);
  }

  @Override
  protected boolean canProcess(float x, float y)
  {
    return isBetween(x, left, right) && isBetween(y, bottom, top);
  }

  private static boolean isBetween(float value, float left, float right)
  {
    return value > left && value < right;
  }
}
