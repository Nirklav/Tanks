package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Game.Menu.IEventProcessor;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable2D.Sprite;

public class Button
  implements I2DRenderable,
             IEventProcessor
{
  private Sprite sprite;

  public Button(float x, float y, float width, float height)
  {
    sprite = new Sprite("button");
    sprite.setTextureCoordinates(0, 0, 1, 1);
    sprite.setPosition(new Vector2(x, y));
    sprite.setSize(width, height);
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    sprite.draw(orthoViewMatrix);
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
}
