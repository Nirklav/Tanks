package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;

public class Button
  extends Control
{
  private boolean state;

  private GLSprite sprite;

  private float[] pressed;
  private float[] notPressed;

  private float left;
  private float right;
  private float bottom;
  private float top;

  public Button(float x, float y, float width, float height)
  {
    left = x - width / 2;
    right = x + width / 2;
    bottom = y - height / 2;
    top = y + height / 2;

    sprite = new GLSprite("button");
    sprite.setPosition(x, y);
    sprite.setSize(width, height);

    pressed = new float[] { 0.5f, 0f, 0.5f, 1f };
    notPressed = new float[] { 0f, 0f, 0.5f, 1f };

    setTextureCoordinates(notPressed);
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

  public void setPressedTextureCoordinates(float x, float y, float width, float height)
  {
    pressed = new float[] { x, y, width, height };
    if (state)
      setTextureCoordinates(pressed);
  }

  public void setNotPressedTextureCoordinates(float x, float y, float width, float height)
  {
    notPressed = new float[] { x, y, width, height };
    if (!state)
      setTextureCoordinates(notPressed);
  }

  private void setTextureCoordinates(float[] texCoords)
  {
    sprite.setTextureCoordinates(texCoords[0], texCoords[1], texCoords[2], texCoords[3]);
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
    setTextureCoordinates(pressed);
  }

  @Override
  public void onUp(float x, float y)
  {
    state = false;
    setTextureCoordinates(notPressed);
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
