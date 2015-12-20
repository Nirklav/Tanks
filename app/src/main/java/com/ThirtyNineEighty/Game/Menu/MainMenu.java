package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.EditorWorld;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.Game.Subprograms.Subprogram;

public class MainMenu
  extends BaseMenu
{
  private GLLabel resourceCacheStatus;

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
        IWorld world = GameContext.content.getWorld();
        if (world == null)
          return;

        if (world instanceof GameWorld)
        {
          world.enable();
          GameContext.content.setMenu(new GameMenu());
        }
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
        GameContext.content.setWorld(null);
        GameContext.content.setMenu(new MapSelectMenu(new GameStartArgs()));
      }
    });
    add(newGameButton);

    Button cacheStatus = new Button("Show cache status");
    cacheStatus.setPosition(0, -20);
    cacheStatus.setSize(600, 200);
    cacheStatus.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        resourceCacheStatus.setVisible(true);
        resourceCacheStatus.setValue(GameContext.resources.getCacheStatus());

        bind(new Subprogram()
        {
          private boolean delayed;

          @Override
          protected void onUpdate()
          {
            if (!delayed)
            {
              delayed = true;
              delay(5000);
              return;
            }

            resourceCacheStatus.setVisible(false);
            unbind();
          }
        });
      }
    });
    add(cacheStatus);

    resourceCacheStatus = new GLLabel(GameContext.resources.getCacheStatus(), MeshMode.Dynamic);
    resourceCacheStatus.setPosition(960, 540);
    resourceCacheStatus.setAlign(GLLabel.AlignType.TopRight);
    resourceCacheStatus.setVisible(false);
    bind(resourceCacheStatus);

    Button editor = new Button("Editor");
    editor.setPosition(0, -240);
    editor.setSize(600, 200);
    editor.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(new EditorWorld());
        GameContext.content.setMenu(new EditorMenu());
      }
    });
    add(editor);
  }
}
