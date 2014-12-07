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

    pressed = new float[] { 0, 0, 1, 1 };
    notPressed = new float[] { 0, 0, 1, 1 };

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

  public void setPressedTextureCoordinates(float x, float y, float width, float height) { pressed = new float[] { x, y, width, height }; }
  public void setNotPressedTextureCoordinates(float x, float y, float width, float height) { notPressed = new float[] { x, y, width, height }; }
  private void setTextureCoordinates(float[] texCoords) { sprite.setTextureCoordinates(texCoords[0], texCoords[1], texCoords[2], texCoords[3]); }

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
    if (isBetween(x, left, right) &&
        isBetween(y, bottom, top))
    {
      state = true;
      this.pointerId = pointerId;

      setTextureCoordinates(pressed);
    }
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
      this.pointerId = 0;

      setTextureCoordinates(notPressed);

      if (clickListener != null)
        clickListener.onClick();
    }
  }

  private static boolean isBetween(float value, float left, float right)
  {
    return value > left && value < right;
  }

  public interface IClickListener
  {
    void onClick();
  }
}
