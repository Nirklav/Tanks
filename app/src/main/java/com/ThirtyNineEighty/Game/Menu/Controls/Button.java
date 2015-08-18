package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Renderable.GL.GLSprite;
import com.ThirtyNineEighty.Resources.Sources.FileImageSource;
import com.ThirtyNineEighty.Resources.Entities.Image;
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

  public Button(String caption) { this(caption, "pressedBtn", "notPressedBtn"); }
  public Button(String caption, String pressedName, String notPressedName)
  {
    pressed = GameContext.resources.getImage(new FileImageSource(pressedName));
    notPressed = GameContext.resources.getImage(new FileImageSource(notPressedName));

    sprite = new GLSprite(notPressed);
    sprite.setZIndex(0);

    label = new GLLabel(caption);
    label.setZIndex(1);

    setSize(200, 100);
  }

  @Override
  public void initialize()
  {
    super.initialize();

    bind(sprite);
    bind(label);
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

  private static boolean isBetween(float value, float min, float  max)
  {
    return value > min && value < max;
  }
}
