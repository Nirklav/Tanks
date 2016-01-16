package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Selector;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapDescription;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.Game.Resources.Sources.FileMapDescriptionSource;

public class MapSelectMenu
  extends BaseMenu
{
  private final GameStartArgs args;

  private Selector mapSelector;
  private GLLabelProvider mapName;
  private GLLabelProvider closed;

  private transient ContentNames maps;

  public MapSelectMenu(final GameStartArgs args)
  {
    this.args = args;
  }

  @Override
  public void initialize()
  {
    super.initialize();

    maps = TanksContext.resources.getContent(new FileContentSource(FileContentSource.maps));
    mapSelector = new Selector(maps.names, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        args.setMapName(current);
        mapName.setValue(current);
        closed.setVisible(!isMapOpen(current));
      }
    });

    Button prevMapButton = new Button("Prev map");
    prevMapButton.setPosition(70, -440);
    prevMapButton.setSize(300, 200);
    prevMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.Prev();
      }
    });
    add(prevMapButton);

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
        if (!isMapOpen(args.getMapName()))
          return;

        TanksContext.content.setMenu(new TankSelectMenu(args));
        TanksContext.content.setWorld(new TankSelectWorld(args));
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
        TanksContext.content.setWorld(null);
        TanksContext.content.setMenu(new MainMenu());
      }
    });
    add(menu);

    mapName = new GLLabelProvider(mapSelector.getCurrent(), 40, 60, MeshMode.Dynamic);
    bind(new GLLabel(mapName));

    closed = new GLLabelProvider("Closed");
    closed.setPosition(0, 100);
    closed.setVisible(!isMapOpen(mapSelector.getCurrent()));
    bind(new GLLabel(closed));
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    TanksContext.resources.release(maps);
  }

  private boolean isMapOpen(String mapName)
  {
    MapDescription mapDescription = TanksContext.resources.getMap(new FileMapDescriptionSource(mapName));
    return mapDescription.openedOnStart || TanksContext.data.isMapOpen(mapName);
  }
}
