package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Selector;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Game.ContentState.States.GameLoadingState;
import com.ThirtyNineEighty.Game.ContentState.States.MainState;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Base.Menus.Controls.Gesture;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.Base.Resources.Sources.FileDescriptionSource;

public class TankSelectMenu
  extends BaseMenu
{
  private Gesture gesture;
  private GLLabelProvider bulletLabel;
  private GLLabelProvider tankLabel;

  private GameStartArgs startArgs;
  private Selector tankSelector;
  private Selector bulletSelector;

  private transient ContentNames tanks;
  private transient ContentNames bullets;

  public TankSelectMenu(GameStartArgs args)
  {
    startArgs = args;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    tanks = TanksContext.resources.getContent(new FileContentSource(FileContentSource.tanks));
    tankSelector = new Selector(tanks.names, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        TankSelectWorld world = (TankSelectWorld) TanksContext.content.getWorld();
        world.setPlayer(current);
        startArgs.setTankName(current);

        tankLabel.setValue(getTankDescription());
        bulletLabel.setValue(getBulletDescription());
      }
    });

    bullets = TanksContext.resources.getContent(new FileContentSource(FileContentSource.bullets));
    bulletSelector = new Selector(bullets.names, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        GameProperties properties = startArgs.getProperties();
        properties.setBullet(current);
        bulletLabel.setValue(getBulletDescription());
      }
    });

    gesture = new Gesture();
    gesture.setGestureListener(new Runnable()
    {
      @Override
      public void run()
      {
        Vector2 vec = gesture.get();
        TankSelectWorld world = (TankSelectWorld) TanksContext.content.getWorld();

        world.addAngle(vec.getX() / 10);
        world.addLength(vec.getY() / 50);

        Vector2.release(vec);
      }
    });
    add(gesture);

    Button menuButton = new Button("Menu");
    menuButton.setPosition(-810, -440);
    menuButton.setSize(300, 200);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.contentState.set(new MainState(true));
      }
    });
    add(menuButton);

    Button nextTankButton = new Button(">");
    nextTankButton.setPosition(875, 460);
    nextTankButton.setSize(150, 150);
    nextTankButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        tankSelector.next();
      }
    });
    add(nextTankButton);

    Button prevTankButton = new Button("<");
    prevTankButton.setPosition(715, 460);
    prevTankButton.setSize(150, 150);
    prevTankButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        tankSelector.prev();
      }
    });
    add(prevTankButton);

    Button nextBulletButton = new Button(">");
    nextBulletButton.setPosition(-715, 460);
    nextBulletButton.setSize(150, 150);
    nextBulletButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        bulletSelector.next();
      }
    });
    add(nextBulletButton);

    Button prevBulletButton = new Button("<");
    prevBulletButton.setPosition(-875, 460);
    prevBulletButton.setSize(150, 150);
    prevBulletButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        bulletSelector.prev();
      }
    });
    add(prevBulletButton);

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

        TanksContext.contentState.set(new GameLoadingState(startArgs));
      }
    });
    add(gameButton);

    bulletLabel = new GLLabelProvider(getBulletDescription(), MeshMode.Dynamic);
    bulletLabel.setAlign(GLLabelProvider.AlignType.TopLeft);
    bulletLabel.setPosition(-950, 330);
    bind(new GLLabel(bulletLabel));

    tankLabel = new GLLabelProvider(getTankDescription(), MeshMode.Dynamic);
    tankLabel.setAlign(GLLabelProvider.AlignType.TopRight);
    tankLabel.setPosition(950, 330);
    bind(new GLLabel(tankLabel));
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    TanksContext.resources.release(tanks);
    TanksContext.resources.release(bullets);
  }

  private String getBulletDescription()
  {
    GameProperties properties = startArgs.getProperties();
    GameDescription bulletDescription = getDescription(properties.getBullet());

    String result = String.format("Available: %s\nBullets: %s\nDamage: %d hp\nSpeed: %d m/s"
      , isBulletAvailable() ? "Yes" : "No"
      , properties.getBullet()
      , (int)bulletDescription.getDamage()
      , (int)bulletDescription.getSpeed()
    );

    GameContext.resources.release(bulletDescription);
    return result;
  }

  private String getTankDescription()
  {
    GameDescription tankDescription = getDescription(startArgs.getTankName());
    String result = String.format("Opened: %s\nTank: %s\nHealth: %d hp\nSpeed: %d m/s\nTurret speed: %d degree/s\nRecharge speed %d per/s"
      , isTankAvailable() ? "Yes" : "No"
      , startArgs.getTankName()
      , (int)tankDescription.getHealth()
      , (int)tankDescription.getSpeed()
      , (int)tankDescription.getTurretRotationSpeed()
      , (int)tankDescription.getRechargeSpeed()
    );

    GameContext.resources.release(tankDescription);
    return result;
  }

  private boolean isBulletAvailable()
  {
    GameProperties properties = startArgs.getProperties();
    GameDescription tankDescription = getDescription(startArgs.getTankName());

    boolean result = tankDescription
      .getSupportedBullets()
      .contains(properties.getBullet());

    GameContext.resources.release(tankDescription);
    return result;
  }

  private boolean isTankAvailable()
  {
    String tankName = startArgs.getTankName();
    GameDescription tankDescription = getDescription(startArgs.getTankName());

    boolean result = tankDescription.openedOnStart() || TanksContext.data.isTankOpen(tankName);

    GameContext.resources.release(tankDescription);
    return result;
  }

  private GameDescription getDescription(String name)
  {
    return (GameDescription) TanksContext.resources.getDescription(new FileDescriptionSource(name));
  }
}
