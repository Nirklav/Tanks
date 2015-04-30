package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLSprite;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

public class ProgressBar
  implements I2DRenderable
{
  private GLSprite edge;
  private GLSprite background;

  private float progress;
  private float maxProgress;

  public ProgressBar()
  {
    edge = new GLSprite("progressBarEdge");
    background = new GLSprite("progressBarBackground");

    progress = 100;
    maxProgress = 100;

    setSize(400, 40);
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    edge.draw(orthoViewMatrix);
    background.draw(orthoViewMatrix);
  }

  public void setSize(float width, float height)
  {
    edge.setSize(width, height);
    background.setSize(width, height);
  }

  public void setPosition(float x, float y)
  {
    edge.setPosition(x, y);
    background.setPosition(x, y);
  }

  public void setMaxProgress(float value)
  {
    maxProgress = value;
    applyProgress();
  }

  public void setProgress(float value)
  {
    progress = value;
    applyProgress();
  }

  private void applyProgress()
  {
    float relativeUnits = progress / maxProgress;

    float width = edge.getWidth() * relativeUnits;
    float height = background.getHeight();

    background.setSize(width, height);
  }
}
