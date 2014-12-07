package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

import java.util.ArrayList;
import java.util.List;

public class GameMenu extends BaseMenu
{
  private ArrayList<I2DRenderable> controls;

  private Button forwardButton;
  private Button leftButton;
  private Button rightButton;

  public GameMenu()
  {
    super();

    controls = new ArrayList<I2DRenderable>();

    addButton(forwardButton = new Button(0, -440, 300, 200));
    addButton(leftButton = new Button(-810, -440, 300, 200));
    addButton(rightButton = new Button(810, -440, 300, 200));
  }

  @Override
  public void fillRenderable(List<I2DRenderable> renderables)
  {
    for(I2DRenderable renderable : controls)
      renderables.add(renderable);
  }

  public boolean getForwardState() { return forwardButton.getState(); }
  public boolean getLeftState() { return leftButton.getState(); }
  public boolean getRightState() { return rightButton.getState(); }

  private void addButton(Button btn)
  {
    controls.add(btn);

    addEventProcessor(btn);

    btn.setNotPressedTextureCoordinates(0f, 0f, 0.5f, 1f);
    btn.setPressedTextureCoordinates(0.5f, 0f, 0.5f, 1f);
  }
}
