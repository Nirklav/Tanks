package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

public class MapSelectMenu
  extends BaseMenu
{
  private final GameStartArgs args;
  private final Selector mapSelector;

  private GLLabel mapName;
  private GLLabel closed;

  public MapSelectMenu(final GameStartArgs args)
  {
    this.args = args;
    this.mapSelector = new Selector("maps", new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        args.setMapName(current);
        mapName.setValue(current);
        closed.setVisible(!GameContext.gameProgress.isMapOpen(current));
      }
    });
  }

  @Override
  public void initialize()
  {
    super.initialize();

    Button prevMap = new Button("Prev map");
    prevMap.setPosition(70, -440);
    prevMap.setSize(300, 200);
    prevMap.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.Prev();
      }
    });
    add(prevMap);

    Button nextMapButton = new Button("Next map");
    nextMapButton.setPosition(390, -440);
    nextMapButton.setSize(300, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.Next();
      }
    });
    add(nextMapButton);

    Button selectTank = new Button("Select tank");
    selectTank.setPosition(760, -440);
    selectTank.setSize(400, 200);
    selectTank.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        if (!GameContext.gameProgress.isMapOpen(args.getMapName()))
          return;

        GameContext.content.setMenu(new TankSelectMenu(args));
        GameContext.content.setWorld(new TankSelectWorld(args));
      }
    });
    add(selectTank);

    Button menu = new Button("Menu");
    menu.setPosition(-810, -440);
    menu.setSize(300, 200);
    menu.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(null);
        GameContext.content.setMenu(new MainMenu());
      }
    });
    add(menu);

    mapName = new GLLabel(mapSelector.getCurrent(), "simpleFont", 40, 60, MeshMode.Dynamic);
    bind(mapName);

    closed = new GLLabel("Closed");
    closed.setPosition(0, 100);
    closed.setVisible(!GameContext.gameProgress.isMapOpen(mapSelector.getCurrent()));
    bind(closed);
  }
}
