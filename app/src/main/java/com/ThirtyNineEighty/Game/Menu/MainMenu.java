package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Renderable.Subprograms.DelayedRenderableSubprogram;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.EditorWorld;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

public class MainMenu
  extends BaseMenu
{
  private GLLabelProvider resourceCacheStatus;

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
        {
          world.enable();
          TanksContext.content.setMenu(new GameMenu());
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
        TanksContext.content.setWorld(null);
        TanksContext.content.setMenu(new MapSelectMenu(new GameStartArgs()));
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
        resourceCacheStatus.setValue(TanksContext.resources.getCacheStatus());

        if (!resourceCacheStatus.isVisible())
        {
          resourceCacheStatus.setVisible(true);
          bind(new DelayedRenderableSubprogram(resourceCacheStatus, 5000));
        }
      }
    });
    add(cacheStatus);

    resourceCacheStatus = new GLLabelProvider(TanksContext.resources.getCacheStatus(), MeshMode.Dynamic);
    resourceCacheStatus.setPosition(960, 540);
    resourceCacheStatus.setAlign(GLLabelProvider.AlignType.TopRight);
    resourceCacheStatus.setVisible(false);
    bind(new GLLabel(resourceCacheStatus));

    Button editor = new Button("Editor");
    editor.setPosition(0, -240);
    editor.setSize(600, 200);
    editor.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.content.setWorld(new EditorWorld());
        TanksContext.content.setMenu(new EditorMenu());
      }
    });
    add(editor);
  }
}
