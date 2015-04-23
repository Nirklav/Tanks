package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Joystick;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Resources.MeshMode;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

public class GameMenu
  extends BaseMenu
{
  private Button leftTurretButton;
  private Button rightTurretButton;

  private GLLabel cacheStatusLabel;

  private Joystick joystick;

  @Override
  public void initialize(Object args)
  {
    Button fireButton = new Button(810, 440, 300, 200);
    fireButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IContent content = GameContext.getContent();
        IWorld world = content.getWorld();

        Tank player = (Tank) world.getPlayer();
        player.fire();
      }
    });

    Button cacheStatusButton = new Button(-810, 440, 300, 200);
    cacheStatusButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        cacheStatusLabel.setValue(Vector.getCacheStatus());
      }
    });

    addControl(rightTurretButton = new Button(800, -290, 150, 150));
    addControl(leftTurretButton = new Button(600, -290, 150, 150));
    addControl(cacheStatusButton);
    addControl(fireButton);

    addControl(joystick = new Joystick(-710, -290, 150));

    cacheStatusLabel = new GLLabel(Vector.getCacheStatus(), "SimpleFont", 50, 60, MeshMode.Dynamic);
    cacheStatusLabel.setPosition(-940, 290);

    addRenderable(cacheStatusLabel);
  }

  public float getJoystickAngle() { return joystick.getVector().getAngle(Vector2.xAxis); }

  public boolean getLeftTurretState() { return leftTurretButton.getState(); }
  public boolean getRightTurretState() { return rightTurretButton.getState(); }
}
