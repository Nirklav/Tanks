package com.ThirtyNineEighty.Base;

import java.util.ArrayList;

import com.ThirtyNineEighty.Base.Common.EventTimer;
import com.ThirtyNineEighty.Base.Common.Stopwatch;
import com.ThirtyNineEighty.Base.Menus.IMenu;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Subprograms.ISubprogram;
import com.ThirtyNineEighty.Base.Subprograms.TaskScheduler;
import com.ThirtyNineEighty.Base.Worlds.IWorld;

public class Content
  implements IStatistics
{
  private volatile IWorld world;
  private volatile IMenu menu;

  private final EventTimer updateTimer;
  private final TaskScheduler taskScheduler;

  private final ArrayList<ISubprogram> subprograms;
  private final ArrayList<Action> subprogramActions;

  private final ArrayList<WorldObject<?, ?>> worldObjects; // memory optimization

  // Statistics
  private final ArrayList<Stopwatch> stopwatches;
  private final Stopwatch subprogramsSw;
  private final Stopwatch collisionsSw;
  private final Stopwatch updateSw;

  public Content()
  {
    subprograms = new ArrayList<>();
    subprogramActions = new ArrayList<>();
    worldObjects = new ArrayList<>();

    stopwatches = new ArrayList<>(3);
    stopwatches.add(subprogramsSw = new Stopwatch("Subprograms", 70));
    stopwatches.add(collisionsSw = new Stopwatch("Collisions", 30));
    stopwatches.add(updateSw = new Stopwatch("Update", 100));

    taskScheduler = new TaskScheduler();
    updateTimer = new EventTimer(
      "update"
      , 20
      , new Runnable()
      {
        @Override
        public void run()
        {
          updateSw.start();

          DeltaTime.update();

          updateSubprograms();
          resolveCollisions();

          if (world != null)
            world.setViews();

          if (menu != null)
            menu.setViews();

          updateSw.stop();
        }
      }
    );
  }

  private void updateSubprograms()
  {
    subprogramsSw.start();

    for (ISubprogram subprogram : subprograms)
    {
      if (!subprogram.isEnabled())
        continue;

      taskScheduler.prepare(subprogram);
      subprogram.update();
    }

    taskScheduler.run();

    for (Action action : subprogramActions)
    {
      switch (action.type)
      {
      case Action.ADD: subprograms.add(action.subprogram); break;
      case Action.REMOVE: subprograms.remove(action.subprogram); break;
      }
    }
    subprogramActions.clear();

    subprogramsSw.stop();
  }

  private void resolveCollisions()
  {
    collisionsSw.start();

    if (world == null)
      return;

    worldObjects.clear();
    world.getObjects(worldObjects);

    for (WorldObject<?, ?> object : worldObjects)
      object.setCollidableLocation();

    GameContext.collisions.resolve(worldObjects);

    collisionsSw.stop();
  }

  public IWorld getWorld() { return world; }
  public void setWorld(IWorld value) { setWorld(value, false); }
  public void setWorldAsync(IWorld value) { setWorld(value, true); }
  private void setWorld(final IWorld value, boolean async)
  {
    Runnable r = new Runnable()
    {
      @Override
      public void run()
      {
        if (world != null)
        {
          if (world.isEnabled())
            world.disable();
          world.uninitialize();
        }

        world = value;
        if (world != null)
        {
          world.initialize();
          world.enable();
        }
      }
    };

    if (async)
      postEvent(r);
    else
      sendEvent(r);
  }


  public IMenu getMenu() { return menu; }
  public void setMenu(IMenu value) { setMenu(value, false); }
  public void setMenuAsync(IMenu value) { setMenu(value, true); }
  private void setMenu(final IMenu value, boolean async)
  {
    Runnable r = new Runnable()
    {
      @Override
      public void run()
      {
        if (menu != null)
        {
          if (menu.isEnabled())
            menu.disable();
          menu.uninitialize();
        }

        menu = value;
        if (menu != null)
        {
          menu.initialize();
          menu.enable();
        }
      }
    };

    if (async)
      postEvent(r);
    else
      sendEvent(r);
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

  public void postEvent(Runnable r) { updateTimer.postEvent(r); }
  public void sendEvent(Runnable r)
  {
    if (GameContext.isMainThread())
      throw new IllegalStateException("can't stop main thread (use post)");

    updateTimer.sendEvent(r);
  }

  public void start() { updateTimer.start(); }
  public void stop() { updateTimer.stop(); }

  public void reset()
  {
    if (updateTimer.isStarted())
      throw new IllegalStateException("can't reset content because timer is started, before use reset call the stop method");

    reset(world);
    reset(menu);
  }

  private static void reset(IEngineObject object)
  {
    if (object == null)
      return;

    if (object.isEnabled())
      object.disable();

    if (object.isInitialized())
      object.uninitialize();
  }

  public String getStatistics()
  {
    StringBuilder builder = new StringBuilder();

    for (Stopwatch stopwatch : stopwatches)
      builder.append(stopwatch).append("\n");

    return builder.toString();
  }

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
