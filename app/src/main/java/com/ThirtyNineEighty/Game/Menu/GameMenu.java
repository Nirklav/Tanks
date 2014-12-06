package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.Collection;

public class GameMenu extends BaseMenu
{
  private ArrayList<I2DRenderable> controls;

  private VerticalEventProcessor forwardProcessor;
  private VerticalEventProcessor leftProcessor;
  private VerticalEventProcessor rightProcessor;

  public GameMenu()
  {
    super();

    Button button = new Button(-910, -490, 100, 100);

    addEventProcessor((forwardProcessor = new VerticalEventProcessor(0.33f, 0.66f)));
    addEventProcessor((leftProcessor = new VerticalEventProcessor(0f, 0.33f)));
    addEventProcessor((rightProcessor = new VerticalEventProcessor(0.66f, 1.0f)));
    addEventProcessor(button);

    controls = new ArrayList<I2DRenderable>();
    controls.add(button);
  }

  @Override
  public Collection<I2DRenderable> getControls()
  {
    return controls;
  }

  public boolean getForwardState()
  {
    return forwardProcessor.getState();
  }

  public boolean getLeftState()
  {
    return leftProcessor.getState();
  }

  public boolean getRightState()
  {
    return rightProcessor.getState();
  }

  private static class VerticalEventProcessor
    implements IEventProcessor
  {
    private float leftBorder;
    private float rightBorder;

    private int pointerId;
    private boolean state;

    public VerticalEventProcessor(float leftBorder, float rightBorder)
    {
      this.leftBorder = leftBorder;
      this.rightBorder = rightBorder;
    }

    public boolean getState()
    {
      return state;
    }

    @Override
    public void processDown(int pointerId, float x, float y)
    {
      float leftBorder = this.leftBorder * GameContext.getWidth();
      float rightBorder = this.rightBorder * GameContext.getWidth();

      if (x > leftBorder && x < rightBorder)
      {
        state = true;
        this.pointerId = pointerId;
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
        this.pointerId = 0;
        state = false;
      }
    }
  }
}
