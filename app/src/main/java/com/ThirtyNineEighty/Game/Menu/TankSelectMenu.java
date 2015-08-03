package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Renderable.GLLabel;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Menu.Controls.Gesture;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.GameContext;

public class TankSelectMenu
  extends BaseMenu
{
  private Gesture gesture;
  private GLLabel bulletLabel;
  private GLLabel tankLabel;

  private GameStartArgs startArgs;
  private Selector tankSelector;
  private Selector bulletSelector;

  public TankSelectMenu(GameStartArgs args)
  {
    startArgs = args;
    tankSelector = new Selector("tanks", new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        TankSelectWorld world = (TankSelectWorld) GameContext.content.getWorld();
        world.setPlayer(current);
        startArgs.setTankName(current);

        tankLabel.setValue(getTankDescription());
        bulletLabel.setValue(getBulletDescription());
      }
    });
    bulletSelector = new Selector("bullets", new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        GameProperties properties = startArgs.getProperties();
        properties.setBullet(current);
        bulletLabel.setValue(getBulletDescription());
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

    Button nextTankButton = new Button(">");
    nextTankButton.setPosition(875, 460);
    nextTankButton.setSize(150, 150);
    nextTankButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        tankSelector.Next();
      }
    });
    addControl(nextTankButton);

    Button prevTankButton = new Button("<");
    prevTankButton.setPosition(715, 460);
    prevTankButton.setSize(150, 150);
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
    nextBulletButton.setPosition(-715, 460);
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
    prevBulletButton.setPosition(-875, 460);
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
        if (!isTankAvailable() || !isBulletAvailable())
          return;

        GameContext.content.setMenu(new GameMenu());
        GameContext.content.setWorld(new GameWorld(startArgs));
      }
    });
    addControl(gameButton);

    bulletLabel = new GLLabel(getBulletDescription(), "simpleFont", 30, 40, MeshMode.Dynamic);
    bulletLabel.setAlign(GLLabel.AlignType.TopLeft);
    bulletLabel.setPosition(-950, 330);
    addRenderable(bulletLabel);

    tankLabel = new GLLabel(getTankDescription(), "simpleFont", 30, 40, MeshMode.Dynamic);
    tankLabel.setAlign(GLLabel.AlignType.TopRight);
    tankLabel.setPosition(950, 330);
    addRenderable(tankLabel);

    super.initialize();
  }

  private String getBulletDescription()
  {
    GameProperties properties = startArgs.getProperties();
    GameDescription bulletDescription = GameContext.resources.getCharacteristic(new FileDescriptionSource(properties.getBullet()));

    return String.format("Available: %s\nBullets: %s\nDamage: %d hp\nSpeed: %d m/s"
      , isBulletAvailable() ? "Yes" : "No"
      , properties.getBullet()
      , (int)bulletDescription.getDamage()
      , (int)bulletDescription.getSpeed()
    );
  }

  private String getTankDescription()
  {
    GameDescription tankDescription = GameContext.resources.getCharacteristic(new FileDescriptionSource(startArgs.getTankName()));
    return String.format("Opened: %s\nTank: %s\nHealth: %d hp\nSpeed: %d m/s\nTurret speed: %d degree/s\nRecharge speed %d per/s"
      , isTankAvailable() ? "Yes" : "No"
      , startArgs.getTankName()
      , (int)tankDescription.getHealth()
      , (int)tankDescription.getSpeed()
      , (int)tankDescription.getTurretRotationSpeed()
      , (int)tankDescription.getRechargeSpeed()
    );
  }

  private boolean isBulletAvailable()
  {
    GameProperties properties = startArgs.getProperties();
    GameDescription tankDescription = GameContext.resources.getCharacteristic(new FileDescriptionSource(startArgs.getTankName()));

    return tankDescription
      .getSupportedBullets()
      .contains(properties.getBullet());
  }

  private boolean isTankAvailable()
  {
    return GameContext.gameProgress.isTankOpen(startArgs.getTankName());
  }
}
