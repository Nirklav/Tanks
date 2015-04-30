package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Joystick;
import com.ThirtyNineEighty.Game.Menu.Controls.ProgressBar;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Resources.MeshMode;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;
import com.ThirtyNineEighty.System.ISubprogram;

public class GameMenu
  extends BaseMenu
{
  private Button leftTurretButton;
  private Button rightTurretButton;

  private GLLabel systemLabel;
  private ProgressBar recharge;
  private ProgressBar health;

  private Joystick joystick;

  @Override
  public void initialize(Object args)
  {
    IContent content = GameContext.getContent();
    content.bindProgram(new ISubprogram()
    {
      @Override
      public void update()
      {
        IContent content = GameContext.getContent();
        IWorld world = content.getWorld();
        Tank player = (Tank) world.getPlayer();
        Characteristic characteristic = player.getCharacteristics();

        health.setProgress(characteristic.getHealth());
        recharge.setProgress(player.getRechargeProgress());
      }
    });

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

    Button cacheStatusButton = new Button("System", "pressedBtn", "notPressedBtn");
    cacheStatusButton.setPosition(-810, 440);
    cacheStatusButton.setSize(300, 200);
    cacheStatusButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        systemLabel.setValue(GameContext.renderableResources.getCacheStatus());
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

    systemLabel = new GLLabel(GameContext.renderableResources.getCacheStatus(), "simpleFont", 25, 40, MeshMode.Dynamic);
    systemLabel.setAlign(GLLabel.AlignType.TopLeft);
    systemLabel.setPosition(-940, 280);
    addRenderable(systemLabel);

    recharge = new ProgressBar();
    recharge.setPosition(220, 520);
    addRenderable(recharge);

    health = new ProgressBar();
    health.setPosition(-220, 520);
    addRenderable(health);
  }

  public float getJoystickAngle() { return joystick.getVector().getAngle(Vector2.xAxis); }

  public boolean getLeftTurretState() { return leftTurretButton.getState(); }
  public boolean getRightTurretState() { return rightTurretButton.getState(); }
}
