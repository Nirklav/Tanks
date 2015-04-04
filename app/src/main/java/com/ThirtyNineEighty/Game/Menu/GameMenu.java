package com.ThirtyNineEighty.Game.Menu;

import android.util.Log;

import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

public class GameMenu extends BaseMenu
{
  private Button forwardButton;
  private Button leftButton;
  private Button rightButton;

  private Button leftTurretButton;
  private Button rightTurretButton;

  @Override
  public void initialize(Object args)
  {
    Button fireButton = new Button(810, 440, 300, 200);
    fireButton.setClickListener(new Button.IClickListener()
    {
      @Override
      public void onClick()
      {
        IContent content = GameContext.getContent();
        IWorld world = content.getWorld();

        Tank player = (Tank) world.getPlayer();
        player.fire();
      }
    });

    Button cacheStatusButton = new Button(-810, 440, 300, 200);
    cacheStatusButton.setClickListener(new Button.IClickListener()
    {
      @Override
      public void onClick()
      {
        Log.d("Cache status", Vector.getCacheStatus());
      }
    });

    addButton(forwardButton = new Button(0, -440, 300, 200));
    addButton(leftButton = new Button(-810, -440, 300, 200));
    addButton(rightButton = new Button(810, -440, 300, 200));
    addButton(rightTurretButton = new Button(885, 0, 150, 100));
    addButton(leftTurretButton = new Button(-885, 0, 150, 100));
    addButton(cacheStatusButton);
    addButton(fireButton);

    addRenderable(new GLLabel("Test text!\nWith new lines\n\tand tabs..\nKu-ku~", "SimpleFont", 100, 100));
  }

  public boolean getForwardState() { return forwardButton.getState(); }
  public boolean getLeftState() { return leftButton.getState(); }
  public boolean getRightState() { return rightButton.getState(); }

  public boolean getLeftTurretState() { return leftTurretButton.getState(); }
  public boolean getRightTurretState() { return rightTurretButton.getState(); }

  private void addButton(Button btn)
  {
    addControl(btn);

    btn.setNotPressedTextureCoordinates(0f, 0f, 0.5f, 1f);
    btn.setPressedTextureCoordinates(0.5f, 0f, 0.5f, 1f);
  }
}
