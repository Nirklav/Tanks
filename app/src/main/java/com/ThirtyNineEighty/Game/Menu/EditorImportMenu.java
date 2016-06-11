package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Selector;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Renderable.Subprograms.DelayedRenderableSubprogram;
import com.ThirtyNineEighty.Game.Common.EditorExporter;
import com.ThirtyNineEighty.Game.Common.LoadException;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Game.ContentState.States.EditorState;
import com.ThirtyNineEighty.Game.TanksContext;

public class EditorImportMenu
  extends BaseMenu
{
  private Selector mapSelector;
  private GLLabelProvider messageLabel;
  private GLLabelProvider mapNameLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    messageLabel = new GLLabelProvider(" ", MeshMode.Dynamic);
    messageLabel.setPosition(0, -100);
    messageLabel.setVisible(false);
    bind(new GLLabel(messageLabel));

    Button menuButton = new Button("Back");
    menuButton.setPosition(-710, 465);
    menuButton.setSize(500, 150);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.contentState.set(new EditorState());
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
          IWorld world = TanksContext.content.getWorld();
          EditorExporter.importMap(world, mapSelector.getCurrent());

          TanksContext.contentState.set(new EditorState());
        }
        catch (LoadException e)
        {
          showMessage(e.getMessage());
        }
      }
    });
    add(apply);

    mapSelector = new Selector(EditorExporter.getMaps(), new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        mapNameLabel.setValue(current);
      }
    });

    mapNameLabel = new GLLabelProvider(mapSelector.getCurrent(), MeshMode.Dynamic);
    bind(new GLLabel(mapNameLabel));

    Button prevMapButton = new Button("Prev map");
    prevMapButton.setPosition(-160, -220);
    prevMapButton.setSize(300, 200);
    prevMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.prev();
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
        mapSelector.next();
      }
    });
    add(nextMapButton);
  }

  private void showMessage(String message)
  {
    messageLabel.setVisible(true);
    messageLabel.setValue(message);
    bind(new DelayedRenderableSubprogram(messageLabel, 5000));
  }
}
