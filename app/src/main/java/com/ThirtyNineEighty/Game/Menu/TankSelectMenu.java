package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Gesture;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.System.GameContext;

public class TankSelectMenu
  extends BaseMenu
{
  private Gesture gesture;
  private GameStartArgs args;

  @Override
  public void initialize(Object obj)
  {
    if (!(obj instanceof GameStartArgs))
      throw new IllegalArgumentException("Illegal args type");

    args = (GameStartArgs) obj;
    args.setTankName(Characteristic.Tank);

    GameContext.content.setWorld(new TankSelectWorld());

    gesture = new Gesture();
    gesture.setGestureListener(new Runnable()
    {
      @Override
      public void run()
      {
        Vector2 vec = gesture.get();
        TankSelectWorld world = (TankSelectWorld) GameContext.content.getWorld();

        world.addAngle(vec.getX() / 10);
        world.addLength(vec.getY() / 50);

        Vector.release(vec);
      }
    });
    addControl(gesture);

    Button menuButton = new Button("Menu");
    menuButton.setPosition(-810, -440);
    menuButton.setSize(300, 200);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(null);
        GameContext.content.setMenu(new MainMenu());
      }
    });
    addControl(menuButton);

    Button nextTankButton = new Button("Next");
    nextTankButton.setPosition(490, -440);
    nextTankButton.setSize(300, 200);
    nextTankButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TankSelectWorld world = (TankSelectWorld) GameContext.content.getWorld();
        String tankName = args.getTankName();

        tankName = Characteristic.Tank.equals(tankName)
          ? Characteristic.SpeedTank
          : Characteristic.Tank;

        args.setTankName(tankName);
        world.setPlayer(tankName);
      }
    });
    addControl(nextTankButton);

    Button gameButton = new Button("Game");
    gameButton.setPosition(810, -440);
    gameButton.setSize(300, 200);
    gameButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(new GameWorld(), args);
      }
    });
    addControl(gameButton);

    super.initialize(args);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();
  }
}
