package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Renderable.Resources.MeshMode;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;

import java.util.List;

public class MainMenu
  extends BaseMenu
{
  private GLLabel label;

  private String selectedMap;
  private int selectedMapIndex;

  @Override
  public void initialize(Object args)
  {
    List<String> maps = GameContext.mapLoader.getMaps();
    selectedMapIndex = 0;
    selectedMap = maps.get(selectedMapIndex);

    Button newGameButton = new Button("New game", "pressedBtn", "notPressedBtn");
    newGameButton.setPosition(0, 200);
    newGameButton.setSize(600, 200);
    newGameButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        GameContext.content.setWorld(new GameWorld(), selectedMap);
      }
    });
    addControl(newGameButton);

    label = new GLLabel(getMapLabel(selectedMap), "simpleFont", 40, 60, MeshMode.Dynamic);
    addRenderable(label);

    Button nextMapButton = new Button("Next map", "pressedBtn", "notPressedBtn");
    nextMapButton.setPosition(0, -200);
    nextMapButton.setSize(600, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override public void run()
      {
        selectedMapIndex++;

        List<String> maps = GameContext.mapLoader.getMaps();
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
    return String.format("Selected map: %s", mapName);
  }
}
