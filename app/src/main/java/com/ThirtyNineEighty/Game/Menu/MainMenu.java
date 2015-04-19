package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Renderable.MeshMode;
import com.ThirtyNineEighty.Renderable.Renderable2D.GLLabel;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;

import java.util.List;

public class MainMenu
  extends BaseMenu
{
  private String selectedMap;
  private int selectedMapIndex;

  @Override
  public void initialize(Object args)
  {
    List<String> maps = GameContext.mapLoader.getMaps();
    selectedMapIndex = 0;
    selectedMap = maps.get(selectedMapIndex);

    Button newGameButton = new Button(0, 150, 600, 200);
    newGameButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IContent content = GameContext.getContent();
        content.setWorld(new GameWorld(), selectedMap);
      }
    });

    GLLabel label = new GLLabel(getMapLabel(selectedMap), "SimpleFont", 50, 50, MeshMode.Dynamic);
    label.setPosition(-250, 0);

    Button nextMapButton = new Button(0, -200, 600, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        selectedMapIndex ++;

        List<String> maps = GameContext.mapLoader.getMaps();
        if (selectedMapIndex >= maps.size())
          selectedMapIndex = 0;

        selectedMap = maps.get(selectedMapIndex);
      }
    });

    addControl(newGameButton);
    addControl(nextMapButton);
    addRenderable(label);
  }

  private static String getMapLabel(String mapName)
  {
    return String.format("Selected map: %s", mapName);
  }
}
