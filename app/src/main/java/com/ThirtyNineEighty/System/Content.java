package com.ThirtyNineEighty.System;

import java.util.ArrayList;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.EventTimer;

public class Content
{
  private volatile IWorld world;
  private volatile IMenu menu;

  private final EventTimer updateTimer;

  private ISubprogram lastSubprogram;
  private final ArrayList<ISubprogram> subprograms;
  private final ArrayList<Action> subprogramActions;

  public Content()
  {
    subprograms = new ArrayList<>();
    subprogramActions = new ArrayList<>();

    updateTimer = new EventTimer(
      "update"
      , 25
      , new Runnable()
      {
        @Override
        public void run()
        {
          GameContext.updateTime();

          for (ISubprogram subprogram : subprograms)
            subprogram.update();

          if (lastSubprogram != null)
            lastSubprogram.update();

          for (Action action : subprogramActions)
          {
            switch (action.type)
            {
            case Action.ADD: subprograms.add(action.subprogram); break;
            case Action.REMOVE: subprograms.remove(action.subprogram); break;
            }
          }
          subprogramActions.clear();
        }
      }
    );
  }

  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();
    updateTimer.stop();
  }

  public IWorld getWorld() { return world; }
  public void setWorld(IWorld value) { setWorld(value, null, false); }
  public void setWorld(IWorld value, Object args) { setWorld(value, args, false); }
  public void setWorldAsync(IWorld value, Object args) { setWorld(value, args, true); }
  private void setWorld(final IWorld value, final Object args, boolean async)
  {
    Runnable r = new Runnable()
    {
      @Override public void run()
      {
        if (world != null)
          world.uninitialize();

        world = value;
        world.initialize(args);
      }
    };

    if (async)
    {
      updateTimer.postEvent(r);
    }
    else
    {
      if (GameContext.isMainThread())
        throw new IllegalStateException("can't stop main thread (use post)");

      updateTimer.sendEvent(r);
    }
  }


  public IMenu getMenu() { return menu; }
  public void setMenu(IMenu value) { setMenu(value, null, false); }
  public void setMenu(IMenu value, Object args) { setMenu(value, args, false); }
  public void setMenuAsync(IMenu value, Object args) { setMenu(value, args, true); }
  private void setMenu(final IMenu value, final Object args, boolean async)
  {
    Runnable r = new Runnable()
    {
      @Override public void run()
      {
        if (menu != null)
          menu.uninitialize();

        menu = value;
        menu.initialize(args);
      }
    };

    if (async)
    {
      updateTimer.postEvent(r);
    }
    else
    {
      if (GameContext.isMainThread())
        throw new IllegalStateException("can't stop main thread (use post)");

      updateTimer.sendEvent(r);
    }
  }

  public void bindProgram(final ISubprogram subprogram)
  {
    updateTimer.postEvent(new Runnable()
    {
      @Override public void run()
      {
        subprogramActions.add(new Action(subprogram, Action.ADD));
      }
    });
  }

  public void unbindProgram(final ISubprogram subprogram)
  {
    updateTimer.postEvent(new Runnable()
    {
      @Override public void run()
      {
        subprogramActions.add(new Action(subprogram, Action.REMOVE));
      }
    });
  }

  public void bindLastProgram(ISubprogram subprogram)
  {
    if (lastSubprogram != null)
      throw new IllegalStateException("last subprogram already set!");

    lastSubprogram = subprogram;
  }

  public void unbindLastProgram()
  {
    lastSubprogram = null;
  }

  public void postEvent(Runnable r) { updateTimer.postEvent(r); }
  public void sendEvent(Runnable r)
  {
    if (GameContext.isMainThread())
      throw new IllegalStateException("can't stop main thread (use post)");

    updateTimer.sendEvent(r);
  }

  public void start() { updateTimer.start(); }
  public void stop() { updateTimer.stop(); }

  private static class Action
  {
    public static final int ADD = 0;
    public static final int REMOVE = 1;

    public final ISubprogram subprogram;
    public final int type;

    public Action(ISubprogram subprogram, int type)
    {
      this.subprogram = subprogram;
      this.type = type;
    }
  }
}
