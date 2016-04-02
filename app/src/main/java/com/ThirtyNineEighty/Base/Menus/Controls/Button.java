package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
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

  private Vector2 position;
  private Vector2 size;

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

    position = new Vector2();
    size = new Vector2();

    setSize(200, 100);
  }

  public void setPosition(float x, float y)
  {
    position.setFrom(x, y);
    sprite.setPosition(x, y);
    label.setPosition(x, y);
  }

  public void setSize(float width, float height)
  {
    size.setFrom(width, height);
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
    float left = position.getX() - size.getX() / 2;
    float right = position.getX() + size.getX() / 2;
    float bottom = position.getY() - size.getY() / 2;
    float top = position.getY() + size.getY() / 2;

    return isBetween(touchX, left, right) && isBetween(touchY, bottom, top);
  }

  private static boolean isBetween(float value, float min, float max)
  {
    return value > min && value < max;
  }
}
