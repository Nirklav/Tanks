package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Game.Menu.IEventProcessor;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable2D.Sprite;

public class Button
  implements I2DRenderable,
             IEventProcessor
{
  private int pointerId;
  private boolean state;

  private Sprite sprite;

  private IClickListener clickListener;

  public Button(float x, float y, float width, float height)
  {
    sprite = new Sprite("button");
    sprite.setPosition(x, y);
    sprite.setSize(width, height);
  }

  public void close()
  {
    sprite.close();
  }

  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();
    sprite.finalize();
  }

  public void setClickListener(IClickListener listener) { clickListener = listener; }
  public boolean getState() { return state; }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    sprite.draw(orthoViewMatrix);
  }

  @Override
  public void processDown(int pointerId, float x, float y)
  {
    state = true;
    this.pointerId = pointerId;
  }

  @Override
  public void processMove(int pointerId, float x, float y)
  {

  }

  @Override
  public void processUp(int pointerId, float x, float y)
  {
    if (this.pointerId == pointerId)
    {
      state = false;

      if (clickListener != null)
        clickListener.onClick();
    }
  }

  public interface IClickListener
  {
    void onClick();
  }
}
