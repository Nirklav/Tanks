package com.ThirtyNineEighty.Game.Menu;

public class GameMenu extends BaseMenu
{
  private VerticalEventProcessor forwardProcessor;
  private VerticalEventProcessor leftProcessor;
  private VerticalEventProcessor rightProcessor;

  public GameMenu()
  {
    super();

    addEventProcessor((forwardProcessor = new VerticalEventProcessor(0.33f, 0.66f)));
    addEventProcessor((leftProcessor = new VerticalEventProcessor(0f, 0.33f)));
    addEventProcessor((rightProcessor = new VerticalEventProcessor(0.66f, 1.0f)));
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
      if (x > leftBorder && x < rightBorder)
      {
        state = true;
        this.pointerId = pointerId;
      }
    }

    @Override
    public void processUp(int pointerId, float x, float y)
    {
      if (this.pointerId == pointerId)
        state = false;
    }
  }
}
