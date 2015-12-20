package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Subprograms.Subprogram;
import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.*;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.System.*;

public class GameMenu
  extends BaseMenu
{
  private Button leftTurretButton;
  private Button rightTurretButton;

  private ProgressBar recharge;
  private ProgressBar health;

  private Joystick joystick;

  private GLLabel winLabel;
  private GLLabel loseLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    bind(new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        Map map = GameContext.mapManager.getMap();
        IWorld world = GameContext.content.getWorld();
        Tank player = (Tank) world.getPlayer();

        GameDescription currentDescription = player.getDescription();

        // Player state
        health.setMaxProgress(currentDescription.getHealth());
        health.setProgress(player.getHealth());
        recharge.setProgress(player.getRechargeProgress());

        // Player control
        processPlayerControl(player);

        // Map state
        switch (map.getState())
        {
        case Map.StateWin:
          winLabel.setVisible(true);
          break;
        case Map.StateLose:
          loseLabel.setVisible(true);
          break;
        }
      }
    });

    Button fireButton = new Button("Fire");
    fireButton.setPosition(725, -50);
    fireButton.setSize(300, 200);
    fireButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IWorld world = GameContext.content.getWorld();
        Tank player = (Tank) world.getPlayer();
        player.fire();
      }
    });
    add(fireButton);

    Button menuButton = new Button("Menu");
    menuButton.setPosition(-810, 440);
    menuButton.setSize(300, 200);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IWorld world = GameContext.content.getWorld();
        world.disable();
        GameContext.content.setMenu(new MainMenu());
      }
    });
    add(menuButton);

    rightTurretButton = new Button("Right");
    rightTurretButton.setPosition(800, -290);
    rightTurretButton.setSize(150, 150);
    add(rightTurretButton);

    leftTurretButton = new Button("Left");
    leftTurretButton.setPosition(600, -290);
    leftTurretButton.setSize(150, 150);
    add(leftTurretButton);

    add(joystick = new Joystick(-710, -290, 150));

    recharge = new ProgressBar();
    recharge.setPosition(220, 520);
    add(recharge);

    health = new ProgressBar();
    health.setPosition(-220, 520);
    add(health);

    winLabel = new GLLabel("WIN");
    winLabel.setCharSize(60, 80);
    winLabel.setVisible(false);
    bind(winLabel);

    loseLabel = new GLLabel("LOSE");
    loseLabel.setCharSize(60, 80);
    loseLabel.setVisible(false);
    bind(loseLabel);
  }

  private void processPlayerControl(Tank player)
  {
    if (player.getHealth() <= 0)
      return;

    float joyAngle = getJoystickAngle();
    float playerAngle = player.getAngles().getZ();

    float deltaAngle = Math.abs(joyAngle - playerAngle);

    // make an adjustment to the angle for the following conditions
    // example: joyAngle = 359 deg
    //          playerAngle = 5 deg
    //
    // delta should be 6 deg
    if (deltaAngle > 180)
      deltaAngle = 360 - deltaAngle;

    // forward
    if (deltaAngle <= 90)
    {
      if (deltaAngle < 45)
        player.move();

      if (deltaAngle > 3)
        player.rotate(joyAngle);
    }
    // back
    else
    {
      if (deltaAngle > 135)
        player.moveBack();

      if (deltaAngle < 177)
        player.rotate(joyAngle - 180);
    }

    // turret
    GameDescription playerDescription = player.getDescription();

    if (leftTurretButton.getState())
      player.addTurretAngle(playerDescription.getTurretRotationSpeed() * GameContext.getDelta());

    if (rightTurretButton.getState())
      player.addTurretAngle(-playerDescription.getTurretRotationSpeed() * GameContext.getDelta());
  }

  private float getJoystickAngle()
  {
    Vector2 vector = joystick.getVector();
    return Vector2.xAxis.getAngle(vector);
  }
}
