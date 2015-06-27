package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;

import java.util.List;

public class MainMenu
  extends BaseMenu
{
  private GLLabel label;

  private List<String> maps;
  private String selectedMap;
  private int selectedMapIndex;

  @Override
  public void initialize(Object args)
  {
    maps = GameContext.mapManager.getMaps();
    selectedMapIndex = 0;
    selectedMap = maps.get(selectedMapIndex);

    Button continueButton = new Button("Continue");
    continueButton.setPosition(0, 400);
    continueButton.setSize(600, 200);
    continueButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        IWorld world = GameContext.content.getWorld();
        if (world != null)
        {
          GameContext.content.setMenu(new GameMenu());
          world.enable();
        }
      }
    });
    addControl(continueButton);

    Button newGameButton = new Button("New game");
    newGameButton.setPosition(0, 150);
    newGameButton.setSize(600, 200);
    newGameButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        if (!GameContext.gameProgress.isMapOpen(selectedMap))
          return;

        GameStartArgs args = new GameStartArgs();
        args.setMapName(selectedMap);
        GameContext.content.setMenu(new TankSelectMenu(), args);
      }
    });
    addControl(newGameButton);

    label = new GLLabel(getMapLabel(selectedMap), "simpleFont", 40, 60, MeshMode.Dynamic);
    addRenderable(label);

    Button nextMapButton = new Button("Next map");
    nextMapButton.setPosition(0, -150);
    nextMapButton.setSize(600, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        selectedMapIndex++;

        if (selectedMapIndex >= maps.size())
          selectedMapIndex = 0;

        selectedMap = maps.get(selectedMapIndex);
        label.setValue(getMapLabel(selectedMap));
      }
    });
    addControl(nextMapButton);

    super.initialize(args);
  }

  private static String getMapLabel(String mapName)
  {
    if (GameContext.gameProgress.isMapOpen(mapName))
      return String.format("Selected map: %s", mapName);

    return String.format("Selected map: %s (Closed)", mapName);
  }
}
