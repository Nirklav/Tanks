package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Joystick;
import com.ThirtyNineEighty.Game.Menu.Controls.ProgressBar;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.Subprogram;

public class GameMenu
  extends BaseMenu
{
  private Button leftTurretButton;
  private Button rightTurretButton;

  private ProgressBar recharge;
  private ProgressBar health;

  private Joystick joystick;

  private ISubprogram menuProgram;

  @Override
  public void initialize(Object args)
  {
    GameContext.content.bindProgram(menuProgram = new Subprogram()
    {
      @Override public void onUpdate()
      {
        IWorld world = GameContext.content.getWorld();
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
      @Override public void run()
      {
        IWorld world = GameContext.content.getWorld();
        Tank player = (Tank) world.getPlayer();
        player.fire();
      }
    });
    addControl(fireButton);

    Button menuButton = new Button("Menu", "pressedBtn", "notPressedBtn");
    menuButton.setPosition(-810, 440);
    menuButton.setSize(300, 200);
    menuButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        IWorld world = GameContext.content.getWorld();
        world.disable();
        GameContext.content.setMenu(new MainMenu());
      }
    });
    addControl(menuButton);

    rightTurretButton = new Button("Right", "pressedBtn", "notPressedBtn");
    rightTurretButton.setPosition(800, -290);
    rightTurretButton.setSize(150, 150);
    addControl(rightTurretButton);

    leftTurretButton = new Button("Left", "pressedBtn", "notPressedBtn");
    leftTurretButton.setPosition(600, -290);
    leftTurretButton.setSize(150, 150);
    addControl(leftTurretButton);

    addControl(joystick = new Joystick(-710, -290, 150));

    recharge = new ProgressBar();
    recharge.setPosition(220, 520);
    addRenderable(recharge);

    health = new ProgressBar();
    health.setPosition(-220, 520);
    addRenderable(health);

    super.initialize(args);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    GameContext.content.unbindProgram(menuProgram);
  }

  public float getJoystickAngle()
  {
    Vector2 vector = joystick.getVector();
    return vector.getAngle(Vector2.xAxis);
  }

  public boolean getLeftTurretState() { return leftTurretButton.getState(); }
  public boolean getRightTurretState() { return rightTurretButton.getState(); }
}
