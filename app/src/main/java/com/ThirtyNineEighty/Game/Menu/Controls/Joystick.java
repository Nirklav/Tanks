package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;

public class Joystick implements IControl
{
  private GLSprite background;
  private GLSprite stick;

  public Joystick(float x, float y, float radius)
  {
    background = new GLSprite("joystick");
    stick = new GLSprite("joystick");
  }

  @Override
  public void processDown(int pointerId, float x, float y)
  {

  }

  @Override
  public void processMove(int pointerId, float x, float y)
  {

  }

  @Override
  public void processUp(int pointerId, float x, float y)
  {

  }

  @Override
  public void dispose()
  {
    background.dispose();
    stick.dispose();
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    background.draw(orthoViewMatrix);
    stick.draw(orthoViewMatrix);
  }
}
