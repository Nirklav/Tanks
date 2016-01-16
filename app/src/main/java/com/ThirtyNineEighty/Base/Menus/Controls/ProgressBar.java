package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.Providers.GLSpriteProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLSprite;

public class ProgressBar
  extends Control
{
  private GLSpriteProvider edge;
  private GLSpriteProvider background;

  private float progress;
  private float maxProgress;
  private float width;
  private float height;

  public ProgressBar()
  {
    edge = new GLSpriteProvider("progressBarEdge");
    background = new GLSpriteProvider("progressBarBackground");

    bind(new GLSprite(edge));
    bind(new GLSprite(background));

    progress = 100;
    maxProgress = 100;

    setSize(400, 40);
  }

  @Override
  protected boolean canProcess(float x, float y)
  {
    return false;
  }

  public void setSize(float width, float height)
  {
    this.width = width;
    this.height = height;

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
    background.setSize(width * relativeUnits, height);
  }
}
