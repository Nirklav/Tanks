package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;
import com.ThirtyNineEighty.Renderable.Resources.FileImageSource;
import com.ThirtyNineEighty.Renderable.Resources.Image;
import com.ThirtyNineEighty.System.GameContext;

public class Button
  extends Control
{
  private boolean state;

  private GLSprite sprite;
  private GLLabel label;

  private Image pressed;
  private Image notPressed;

  private float x;
  private float y;
  private float width;
  private float height;

  public Button(String caption, String pressedName, String notPressedName)
  {
    pressed = GameContext.renderableResources.getImage(new FileImageSource(pressedName));
    notPressed = GameContext.renderableResources.getImage(new FileImageSource(notPressedName));

    sprite = new GLSprite(notPressedName);
    sprite.setZIndex(0);

    label = new GLLabel(caption, "simpleFont");
    label.setZIndex(1);

    setSize(200, 100);
  }

  public void setPosition(float x, float y)
  {
    this.x = x;
    this.y = y;

    sprite.setPosition(x, y);
    label.setPosition(x, y);
  }

  public void setSize(float width, float height)
  {
    this.width = width;
    this.height = height;

    sprite.setSize(width, height);
  }

  public boolean getState()
  {
    return state;
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    sprite.draw(orthoViewMatrix);
    label.draw(orthoViewMatrix);
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
  protected boolean canProcess(float touchX, float touchY)
  {
    float left = x - width / 2;
    float right = x + width / 2;
    float bottom = y - height / 2;
    float top = y + height / 2;

    return isBetween(touchX, left, right) && isBetween(touchY, bottom, top);
  }

  private static boolean isBetween(float value, float left, float right)
  {
    return value > left && value < right;
  }
}
