package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Gesture;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.Resources.Sources.FileCharacteristicSource;
import com.ThirtyNineEighty.System.GameContext;

public class TankSelectMenu
  extends BaseMenu
{
  private Gesture gesture;
  private GLLabel closedTankLabel;
  private GLLabel bulletLabel;
  private GameStartArgs args;
  private Selector tankSelector;
  private Selector bulletSelector;

  public TankSelectMenu(final GameStartArgs args)
  {
    this.args = args;
    this.tankSelector = new Selector("tanks", new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        TankSelectWorld world = (TankSelectWorld) GameContext.content.getWorld();
        closedTankLabel.setVisible(!GameContext.gameProgress.isTankOpen(current));
        world.setPlayer(current);
        args.setTankName(current);
      }
    });
    this.bulletSelector = new Selector("bullets", new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        args.setBulletName(current);
        bulletLabel.setValue(getBulletDescription(current));
      }
    });
  }

  @Override
  public void initialize()
  {
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
        tankSelector.Next();
      }
    });
    addControl(nextTankButton);

    Button prevTankButton = new Button("Prev");
    prevTankButton.setPosition(170, -440);
    prevTankButton.setSize(300, 200);
    prevTankButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        tankSelector.Prev();
      }
    });
    addControl(prevTankButton);

    Button nextBulletButton = new Button(">");
    nextBulletButton.setPosition(-715, 455);
    nextBulletButton.setSize(150, 150);
    nextBulletButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        bulletSelector.Next();
      }
    });
    addControl(nextBulletButton);

    Button prevBulletButton = new Button("<");
    prevBulletButton.setPosition(-875, 455);
    prevBulletButton.setSize(150, 150);
    prevBulletButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        bulletSelector.Prev();
      }
    });
    addControl(prevBulletButton);

    Button gameButton = new Button("Game");
    gameButton.setPosition(810, -440);
    gameButton.setSize(300, 200);
    gameButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        if (!GameContext.gameProgress.isTankOpen(args.getTankName()))
          return;

        GameContext.content.setMenu(new GameMenu());
        GameContext.content.setWorld(new GameWorld(args));
      }
    });
    addControl(gameButton);

    closedTankLabel = new GLLabel("Tank closed");
    closedTankLabel.setAlign(GLLabel.AlignType.TopCenter);
    closedTankLabel.setPosition(0, 440);
    closedTankLabel.setVisible(false);
    addRenderable(closedTankLabel);

    bulletLabel = new GLLabel(getBulletDescription(args.getBulletName()), "simpleFont", 30, 40, MeshMode.Dynamic);
    bulletLabel.setAlign(GLLabel.AlignType.TopLeft);
    bulletLabel.setPosition(-950, 330);
    addRenderable(bulletLabel);

    super.initialize();
  }

  private String getBulletDescription(String name)
  {
    Characteristic bulletCh = GameContext.resources.getCharacteristic(new FileCharacteristicSource(name));
    return String.format("Bullets: %s\nDamage: %d\nSpeed: %d"
      , name
      , (int)bulletCh.getDamage()
      , (int)bulletCh.getSpeed()
    );
  }
}
