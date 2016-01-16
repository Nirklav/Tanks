package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Providers.GLSpriteProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Renderable.GL.GLSprite;

public class Button
  extends Control
{
  protected boolean state;

  protected String pressedImageName;
  protected String notPressedImageName;

  protected GLSpriteProvider sprite;
  protected GLLabelProvider label;

  private float x;
  private float y;
  private float width;
  private float height;

  public Button(String caption) { this(caption, "pressedBtn", "notPressedBtn"); }
  public Button(String caption, String pressedImageName, String notPressedImageName)
  {
    this.pressedImageName = pressedImageName;
    this.notPressedImageName = notPressedImageName;

    sprite = new GLSpriteProvider(notPressedImageName);
    sprite.setZIndex(0);
    bind(new GLSprite(sprite));

    label = new GLLabelProvider(caption);
    label.setZIndex(1);
    bind(new GLLabel(label));

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
  public void onDown(float x, float y)
  {
    state = true;
    sprite.setImage(pressedImageName);
  }

  @Override
  public void onUp(float x, float y)
  {
    state = false;
    sprite.setImage(notPressedImageName);
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
