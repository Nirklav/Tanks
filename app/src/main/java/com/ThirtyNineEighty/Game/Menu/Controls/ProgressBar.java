package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.GL.GLSprite;

public class ProgressBar
  extends Control
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
  public void initialize()
  {
    super.initialize();

    addRenderable(edge);
    addRenderable(background);
  }

  @Override
  protected boolean canProcess(float x, float y)
  {
    return false;
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
