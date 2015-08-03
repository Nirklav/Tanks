package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.GLLabel;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Joystick;
import com.ThirtyNineEighty.Game.Menu.Controls.ProgressBar;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

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
    bindProgram(new Subprogram()
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
        Vector3 vector = Vector.getInstance(3);

        float joyAngle = getJoystickAngle();
        float playerAngle = player.getAngles().getZ();

        if (Math.abs(joyAngle - playerAngle) < 90)
          GameContext.collisions.move(player);

        if (Math.abs(joyAngle - playerAngle) > 3)
          GameContext.collisions.rotate(player, joyAngle);

        GameDescription playerDescription = player.getDescription();
        if (leftTurretButton.getState())
          player.addTurretAngle(playerDescription.getTurretRotationSpeed() * GameContext.getDelta());

        if (rightTurretButton.getState())
          player.addTurretAngle(-playerDescription.getTurretRotationSpeed() * GameContext.getDelta());

        Vector.release(vector);

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
    addControl(fireButton);

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
    addControl(menuButton);

    rightTurretButton = new Button("Right");
    rightTurretButton.setPosition(800, -290);
    rightTurretButton.setSize(150, 150);
    addControl(rightTurretButton);

    leftTurretButton = new Button("Left");
    leftTurretButton.setPosition(600, -290);
    leftTurretButton.setSize(150, 150);
    addControl(leftTurretButton);

    addControl(joystick = new Joystick(-710, -290, 150));

    recharge = new ProgressBar();
    recharge.setPosition(220, 520);
    addControl(recharge);

    health = new ProgressBar();
    health.setPosition(-220, 520);
    addControl(health);

    winLabel = new GLLabel("WIN");
    winLabel.setCharSize(60, 80);
    winLabel.setVisible(false);
    addRenderable(winLabel);

    loseLabel = new GLLabel("LOSE");
    loseLabel.setCharSize(60, 80);
    loseLabel.setVisible(false);
    addRenderable(loseLabel);

    super.initialize();
  }

  public float getJoystickAngle()
  {
    Vector2 vector = joystick.getVector();
    return vector.getAngle(Vector2.xAxis);
  }
}
