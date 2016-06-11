package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.IStatistics;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Renderable.Subprograms.DelayedRenderableSubprogram;
import com.ThirtyNineEighty.Game.ContentState.States.EditorState;
import com.ThirtyNineEighty.Game.ContentState.States.GameState;
import com.ThirtyNineEighty.Game.ContentState.States.MapSelectState;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

import java.util.ArrayList;

public class MainMenu
  extends BaseMenu
{
  private GLLabelProvider statsLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    Button continueButton = new Button("Continue");
    continueButton.setPosition(0, 420);
    continueButton.setSize(600, 200);
    continueButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IWorld world = TanksContext.content.getWorld();
        if (world == null)
          return;

        if (world instanceof GameWorld)
          TanksContext.contentState.set(new GameState(world));
      }
    });
    add(continueButton);

    Button newGameButton = new Button("Solo");
    newGameButton.setPosition(0, 200);
    newGameButton.setSize(600, 200);
    newGameButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.contentState.set(new MapSelectState());
      }
    });
    add(newGameButton);

    Button statsButton = new Button("Show statistics");
    statsButton.setPosition(0, -20);
    statsButton.setSize(600, 200);
    statsButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        ArrayList<IStatistics> statistics = new ArrayList<>();
        statistics.add(TanksContext.resources);
        statistics.add(GameContext.content);
        statistics.add(Vector2.pool);
        statistics.add(Vector3.pool);

        IWorld world = GameContext.content.getWorld();
        if (world != null)
        {
          IMap map = world.getMap();
          if (map != null)
            statistics.add(map);
        }

        StringBuilder result = new StringBuilder();
        for (IStatistics stats : statistics)
        {
          result
            .append(stats.getStatistics())
            .append('\n');
        }

        statsLabel.setValue(result.toString());

        if (!statsLabel.isVisible())
        {
          statsLabel.setVisible(true);
          bind(new DelayedRenderableSubprogram(statsLabel, 5000));
        }
      }
    });
    add(statsButton);

    statsLabel = new GLLabelProvider(null, MeshMode.Dynamic);
    statsLabel.setPosition(960, 540);
    statsLabel.setAlign(GLLabelProvider.AlignType.TopRight);
    statsLabel.setVisible(false);
    bind(new GLLabel(statsLabel));

    Button editor = new Button("Editor");
    editor.setPosition(0, -240);
    editor.setSize(600, 200);
    editor.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.contentState.set(new EditorState());
      }
    });
    add(editor);
  }
}
