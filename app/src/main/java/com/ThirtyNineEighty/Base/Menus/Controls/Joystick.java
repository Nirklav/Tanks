package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Renderable.GL.GLSprite;

public class Joystick
  extends Control
{
  private float radius;

  private GLSprite stickSprite;

  private Vector2 position;
  private Vector2 stickPosition;

  private Vector2 vector;

  public Joystick(float x, float y, float radius)
  {
    this.radius = radius;

    position = Vector.getInstance(2);
    position.setFrom(x, y);

    stickPosition = Vector.getInstance(2);
    stickPosition.setFrom(x, y);

    vector = Vector.getInstance(2);

    GLSprite backgroundSprite = new GLSprite("joyBackground");
    backgroundSprite.setSize(radius * 2, radius * 2);
    backgroundSprite.setPosition(position);
    bind(backgroundSprite);

    stickSprite = new GLSprite("joyStick");
    stickSprite.setSize(radius / 2, radius / 2);
    stickSprite.setPosition(stickPosition);
    bind(stickSprite);
  }

  public Vector2 getVector()
  {
    vector.setFrom(position);
    vector.subtract(stickPosition);
    return vector;
  }

  @Override
  public void onDown(float x, float y)
  {
    setStick(x, y);
  }

  @Override
  public void onMove(float x, float y)
  {
    setStick(x, y);
  }

  @Override
  public void onUp(float x, float y)
  {
    resetStick();
  }

  @Override
  protected boolean canProcess(float x, float y)
  {
    float length = getLength(x, y);
    return length < radius;
  }

  private void setStick(float x, float y)
  {
    float stickRadius = stickSprite.getWidth() / 2f;
    float coeff = radius / (radius - stickRadius);
    float xCat = x - position.getX();
    float yCat = y - position.getY();
    float length = (float) Math.sqrt(xCat * xCat + yCat * yCat) * coeff;

    if (length > radius)
    {
      x = position.getX() + xCat * radius / length;
      y = position.getY() + yCat * radius / length;
    }

    stickPosition.setFrom(x, y);
    stickSprite.setPosition(x, y);
  }

  private void resetStick()
  {
    stickPosition.setFrom(position);
    stickSprite.setPosition(position);
  }

  private float getLength(float x, float y)
  {
    float xCat = position.getX() - x;
    float yCat = position.getY() - y;

    return (float) Math.sqrt(xCat * xCat + yCat * yCat);
  }
}
