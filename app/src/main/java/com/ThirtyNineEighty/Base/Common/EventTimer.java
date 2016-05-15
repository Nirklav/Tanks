package com.ThirtyNineEighty.Base.Common;

import java.util.ArrayDeque;

public class EventTimer
{
  private final long MinSleepTime = 10;

  private final Object syncObject;
  private final Runnable userRunnable;
  private final Runnable timerRunnable;
  private final ArrayDeque<Event> events;
  private final int period;
  private long lastExecute;

  private volatile boolean stopping;

  private Thread thread;
  private String threadName;

  public boolean isStarted() { return thread != null; }

  public EventTimer(String name, int periodMs, Runnable r)
  {
    period = periodMs;
    userRunnable = r;
    threadName = name;

    lastExecute = System.currentTimeMillis();
    syncObject = new Object();
    events = new ArrayDeque<>();

    timerRunnable = new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          while (true)
          {
            while (true)
            {
              while (true)
              {
                if (stopping)
                  return;

                Event event;
                synchronized (syncObject)
                {
                  event = events.poll();
                  if (event == null)
                    break;
                }

                event.dispatch();

                if (timeToRun())
                  break;
              } // end of event cycle

              synchronized (syncObject)
              {
                long sleepTime = getSleepTime();
                if (sleepTime > MinSleepTime)
                  syncObject.wait(sleepTime);

                if (timeToRun())
                  break;
              }

            } // end of sleep cycle

            lastExecute = System.currentTimeMillis();
            userRunnable.run();
          } // end of runnable cycle
        }
        catch (Exception e)
        {
          throw new RuntimeException(e);
        }
      }
    };
  }

  private boolean timeToRun()
  {
    return getSleepTime() <= MinSleepTime;
  }

  private long getSleepTime()
  {
    long sleepTime = period;
    long timeLeft = System.currentTimeMillis() - lastExecute;
    long remaining = sleepTime - timeLeft;
    if (remaining < period)
      sleepTime = remaining;

    return sleepTime;
  }

  public void start()
  {
    if (isStarted())
      throw new IllegalStateException("timer already started");

    thread = new Thread(timerRunnable);
    thread.setName(threadName);
    thread.start();
  }

  public void stop()
  {
    stop(true);
  }

  public void stop(boolean clear)
  {
    if (!isStarted())
      throw new IllegalStateException("timer not started");

    try
    {
      stopping = true;
      synchronized (syncObject)
      {
        if (clear)
          events.clear();

        syncObject.notifyAll();
      }

      thread.join();
      thread = null;
      stopping = false;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void postEvent(Runnable r) { addEvent(r, true); }
  public void sendEvent(Runnable r) { addEvent(r, false); }
  private void addEvent(Runnable r, boolean async)
  {
    if (!isStarted() && !async)
      throw new IllegalStateException("Can't wait, timer not started");

    Thread current = Thread.currentThread();
    if (isStarted() && thread.getId() == current.getId())
    {
      r.run();
      return;
    }

    Event event;
    synchronized (syncObject)
    {
      events.add(event = new Event(r, async));
      syncObject.notifyAll();
    }

    event.waitOne();
  }
}

class Event
{
  private final Object syncObject;
  private volatile boolean open;

  public final Runnable runnable;

  public Event(Runnable r, boolean async)
  {
    if (r == null)
      throw new IllegalArgumentException("r");

    runnable = r;

    syncObject = new Object();
    open = async;
  }

  public void waitOne()
  {
    try
    {
      synchronized (syncObject)
      {
        while (!open)
          syncObject.wait();
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void dispatch()
  {
    runnable.run();

    synchronized (syncObject)
    {
      open = true;
      syncObject.notifyAll();
    }
  }
}
