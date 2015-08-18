package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Renderable.GL.GLSprite;

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
    vector.multiplyToY(-1);
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
