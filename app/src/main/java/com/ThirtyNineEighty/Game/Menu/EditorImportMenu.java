package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Common.EditorExporter;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Subprograms.Subprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;

public class EditorImportMenu
  extends BaseMenu
{
  private Selector mapSelector;
  private GLLabel messageLabel;
  private GLLabel mapNameLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    messageLabel = new GLLabel(" ", MeshMode.Dynamic);
    messageLabel.setPosition(0, -100);
    messageLabel.setVisible(false);
    bind(messageLabel);

    Button menuButton = new Button("Back");
    menuButton.setPosition(-710, 465);
    menuButton.setSize(500, 150);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setMenu(new EditorMenu());
      }
    });
    add(menuButton);

    Button apply = new Button("Load");
    apply.setPosition(710, 465);
    apply.setSize(500, 150);
    apply.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IWorld world = GameContext.content.getWorld();
          EditorExporter.importMap(world, mapSelector.getCurrent());
          GameContext.content.setMenu(new EditorMenu());
        }
        catch (IOException e)
        {
          showMessage(e.getMessage());
        }
      }
    });
    add(apply);

    mapNameLabel = new GLLabel(" ", MeshMode.Dynamic);
    bind(mapNameLabel);

    mapSelector = new Selector(EditorExporter.getMaps(), new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        mapNameLabel.setValue(current);
      }
    });

    Button prevMapButton = new Button("Prev map");
    prevMapButton.setPosition(-160, -220);
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
    nextMapButton.setPosition(160, -220);
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
  }

  private void showMessage(String message)
  {
    messageLabel.setVisible(true);
    messageLabel.setValue(message);

    bind(new Subprogram()
    {
      boolean delayed;

      @Override
      protected void onUpdate()
      {
        if (!delayed)
        {
          delay(5000);
          delayed = true;
          return;
        }

        messageLabel.setVisible(false);
        unbind();
      }
    });
  }
}
