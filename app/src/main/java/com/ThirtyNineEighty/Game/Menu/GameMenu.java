package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Menus.Controls.*;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Game.ContentState.States.MainState;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;

public class GameMenu
  extends BaseMenu
{
  private Button leftTurretButton;
  private Button rightTurretButton;

  private ProgressBar recharge;
  private ProgressBar health;

  private Joystick joystick;

  private GLLabelProvider winLabel;
  private GLLabelProvider loseLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    bind(new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        GameWorld world = (GameWorld) TanksContext.content.getWorld();
        Tank player = (Tank) world.getPlayer();
        GameDescription playerDescription = player.getDescription();

        // Player state
        health.setMaxProgress(playerDescription.getHealth());
        health.setProgress(player.getHealth());
        recharge.setProgress(player.getRechargeProgress());

        // Player control
        processPlayerControl(player);

        // Map state
        switch (world.getState())
        {
        case GameWorld.win:
          winLabel.setVisible(true);
          break;
        case GameWorld.lose:
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
        IWorld world = TanksContext.content.getWorld();
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
        TanksContext.contentState.set(new MainState(false));
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

    winLabel = new GLLabelProvider("WIN");
    winLabel.setCharSize(60, 80);
    winLabel.setVisible(false);
    bind(new GLLabel(winLabel));

    loseLabel = new GLLabelProvider("LOSE");
    loseLabel.setCharSize(60, 80);
    loseLabel.setVisible(false);
    bind(new GLLabel(loseLabel));
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
      {
        Vector3 targetAngles = Vector3.getInstance(0, 0, joyAngle);
        player.rotateTo(targetAngles);
        Vector3.release(targetAngles);
      }
    }
    // back
    else
    {
      if (deltaAngle > 135)
        player.moveBack();

      if (deltaAngle < 177)
      {
        Vector3 targetAngles = Vector3.getInstance(0, 0, joyAngle - 180);
        player.rotateTo(targetAngles);
        Vector3.release(targetAngles);
      }
    }

    // turret
    GameDescription playerDescription = player.getDescription();

    if (leftTurretButton.getState())
      player.addTurretAngle(playerDescription.getTurretRotationSpeed() * DeltaTime.get());

    if (rightTurretButton.getState())
      player.addTurretAngle(-playerDescription.getTurretRotationSpeed() * DeltaTime.get());
  }

  private float getJoystickAngle()
  {
    Vector2 vector = joystick.getVector();
    return Vector2.xAxis.getAngle(vector);
  }
}
