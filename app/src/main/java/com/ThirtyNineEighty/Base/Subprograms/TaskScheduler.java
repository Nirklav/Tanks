package com.ThirtyNineEighty.Base.Subprograms;

import android.support.annotation.NonNull;

import com.ThirtyNineEighty.Base.Common.Stopwatch;
import com.ThirtyNineEighty.Base.GameContext;

import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TaskScheduler
{
  private static final int MaxRunTime = 15;
  private static final int CriticalRunTime = -1;

  // Tasks data
  private final TaskAdder adder;
  private final PriorityBlockingQueue<Task> tasks;

  // Threads data
  private final int processors;
  private final Future<?>[] futures;
  private final Stopwatch[] stopwatches;

  // Stats
  private Watcher watcher;

  public TaskScheduler()
  {
    adder = new TaskAdder();
    tasks = new PriorityBlockingQueue<>();

    processors = Runtime
      .getRuntime()
      .availableProcessors();

    futures = new Future[processors];
    stopwatches = new Stopwatch[processors];
    for (int i = 0; i < processors; i++)
      stopwatches[i] = new Stopwatch("TaskScheduler.Worker-" + i, CriticalRunTime);

    watcher = new Watcher(TimeUnit.MILLISECONDS.toNanos(MaxRunTime));
  }

  public void prepare(ISubprogram subprogram)
  {
    if (subprogram == null)
      throw new IllegalArgumentException("subprogram == null");

    subprogram.prepare(adder);
  }

  public void run()
  {
    if (!watcher.tryStart())
      return;
    
    try
    {
      int size = tasks.size();
      if (size == 0)
        return;

      if (size == 1 || processors == 1)
        runImpl(stopwatches[0]);
      else
        runParallel();
    }
    finally
    {
      watcher.stop();
    }
  }

  private void runImpl(Stopwatch stopwatch)
  {
    stopwatch.start();

    while (true)
    {
      Task task = tasks.poll();
      if (task == null)
        break;

      task.run();

      if (stopwatch.elapsed() >= MaxRunTime)
        break;
    }

    stopwatch.stop();
  }

  private void runParallel()
  {
    // Submit tasks
    for (int i = 0; i < processors; i++)
    {
      final int num = i;
      futures[i] = GameContext.threadPool.submit(new Runnable()
      {
        @Override
        public void run()
        {
          runImpl(stopwatches[num]);
        }
      });
    }

    // Wait tasks
    try
    {
      for (int i = 0; i < processors; i++)
        futures[i].get();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private class TaskAdder
    implements ITaskAdder
  {
    public ITask schedule(int priority, Runnable task)
    {
      if (task == null)
        throw new IllegalArgumentException("task == null");

      Task result = new Task(priority, task);
      tasks.add(result);

      return result;
    }
  }
}

class Task
  implements ITask,
             Comparable<Task>
{
  private static long lastId;

  private long id;
  private int priority;
  private Runnable task;
  private boolean isCompleted;

  public Task(int priority, Runnable task)
  {
    if (task == null)
      throw new IllegalArgumentException("task == null");

    this.id = lastId++;
    this.priority = priority;
    this.task = task;
  }

  public void run()
  {
    task.run();
    isCompleted = true;
  }

  @Override
  public boolean isCompleted()
  {
    return isCompleted;
  }

  @Override
  public int compareTo(@NonNull Task another)
  {
    return another.priority - priority;
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (o == this)
      return true;
    if (o instanceof Task)
    {
      Task other = (Task) o;
      return other.id == id;
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return (int)(id ^ (id>>>32));
  }
}

class Watcher
{
  private boolean initialized;

  private static final long secondNanos = 1000 * 1000;

  private long maxNanosPerSec;
  private long startSecondNanos;
  private long elapsedNanos;
  private long startNanos;

  public Watcher(long maxNanosPerSec)
  {
    this.maxNanosPerSec = maxNanosPerSec;
  }

  public boolean tryStart()
  {
    if (!initialized)
    {
      initialized = true;
      startNanos = System.nanoTime();
      startSecondNanos = startNanos;
      return true;
    }

    long currentNanos = System.nanoTime();

    startNanos = currentNanos;
    if (secondNanos < currentNanos - startSecondNanos)
    {
      startSecondNanos = startNanos;
      elapsedNanos = 0;
    }

    return elapsedNanos <= maxNanosPerSec;
  }

  public void stop()
  {
    long endNanos = System.nanoTime();
    elapsedNanos += endNanos - startNanos;
  }
}
