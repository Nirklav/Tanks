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
    Button fireButton = new Button("Fire", "pressedBtn", "notPressedBtn");
    fireButton.setPosition(810, 440);
    fireButton.setSize(300, 200);
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
    addControl(fireButton);

    Button cacheStatusButton = new Button("Get cache", "pressedBtn", "notPressedBtn");
    cacheStatusButton.setPosition(-810, 440);
    cacheStatusButton.setSize(300, 200);
    cacheStatusButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        cacheStatusLabel.setValue(Vector.getCacheStatus());
      }
    });
    addControl(cacheStatusButton);

    rightTurretButton = new Button("Right", "pressedBtn", "notPressedBtn");
    rightTurretButton.setPosition(800, -290);
    rightTurretButton.setSize(150, 150);
    addControl(rightTurretButton);

    leftTurretButton = new Button("Left", "pressedBtn", "notPressedBtn");
    leftTurretButton.setPosition(600, -290);
    leftTurretButton.setSize(150, 150);
    addControl(leftTurretButton);

    addControl(joystick = new Joystick(-710, -290, 150));

    cacheStatusLabel = new GLLabel(Vector.getCacheStatus(), "simpleFont", 25, 40, MeshMode.Dynamic);
    cacheStatusLabel.setAlign(GLLabel.AlignType.TopLeft);
    cacheStatusLabel.setPosition(-940, 280);
    addRenderable(cacheStatusLabel);
  }

  public float getJoystickAngle() { return joystick.getVector().getAngle(Vector2.xAxis); }

  public boolean getLeftTurretState() { return leftTurretButton.getState(); }
  public boolean getRightTurretState() { return rightTurretButton.getState(); }
}
