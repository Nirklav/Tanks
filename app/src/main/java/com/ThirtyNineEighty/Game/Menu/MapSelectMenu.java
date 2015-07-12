package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.TankSelectWorld;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

import java.util.List;

public class MapSelectMenu
  extends BaseMenu
{
  private List<String> maps;
  private int selectedMapIndex;

  private GLLabel mapName;
  private GLLabel closed;

  @Override
  public void initialize()
  {
    maps = GameContext.mapManager.getMaps();
    selectedMapIndex = 0;

    Button prevMap = new Button("Prev map");
    prevMap.setPosition(70, -440);
    prevMap.setSize(300, 200);
    prevMap.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        setMap(-1);
      }
    });
    addControl(prevMap);

    Button nextMapButton = new Button("Next map");
    nextMapButton.setPosition(390, -440);
    nextMapButton.setSize(300, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        setMap(1);
      }
    });
    addControl(nextMapButton);

    Button selectTank = new Button("Select tank");
    selectTank.setPosition(760, -440);
    selectTank.setSize(400, 200);
    selectTank.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        String selectedMap = maps.get(selectedMapIndex);
        if (!GameContext.gameProgress.isMapOpen(selectedMap))
          return;

        GameStartArgs args = new GameStartArgs();
        args.setMapName(selectedMap);

        GameContext.content.setMenu(new TankSelectMenu(args));
        GameContext.content.setWorld(new TankSelectWorld());
      }
    });
    addControl(selectTank);

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
    addControl(menu);

    mapName = new GLLabel(maps.get(selectedMapIndex), "simpleFont", 40, 60, MeshMode.Dynamic);
    addRenderable(mapName);

    closed = new GLLabel("Closed");
    closed.setPosition(0, 100);
    closed.setVisible(false);
    addRenderable(closed);

    super.initialize();
  }

  private void setMap(int delta)
  {
    selectedMapIndex += delta;
    if (selectedMapIndex < 0)
      selectedMapIndex = maps.size() - 1;
    if (selectedMapIndex >= maps.size())
      selectedMapIndex = 0;

    String selectedMap = maps.get(selectedMapIndex);

    mapName.setValue(selectedMap);
    closed.setVisible(!GameContext.gameProgress.isMapOpen(selectedMap));
  }
}
