package com.ThirtyNineEighty.Base.Subprograms;

import android.support.annotation.NonNull;

import com.ThirtyNineEighty.Base.Common.Stopwatch;
import com.ThirtyNineEighty.Base.GameContext;

import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskScheduler
{
  private static final int MaxRunTime = 10;
  private static final int CriticalRunTime = -1;

  private final TaskAdder adder;
  private final PriorityBlockingQueue<Task> tasks;
  private final Stopwatch[] stopwatches;
  private final Future<?>[] futures;
  private final int processors;

  public TaskScheduler()
  {
    adder = new TaskAdder();
    tasks = new PriorityBlockingQueue<>();

    Runtime runtime = Runtime.getRuntime();
    int processorsCount = runtime.availableProcessors();

    stopwatches = new Stopwatch[processorsCount];
    for (int i = 0; i < processorsCount; i++)
      stopwatches[i] = new Stopwatch("TaskScheduler.Worker-" + i, CriticalRunTime);

    futures = new Future[processorsCount];
    processors = processorsCount;
  }

  public void prepare(ISubprogram subprogram)
  {
    if (subprogram == null)
      throw new IllegalArgumentException("subprogram == null");

    subprogram.prepare(adder);
  }

  public void run()
  {
    int size = tasks.size();
    if (size == 0)
      return;

    if (size == 1 || processors == 1)
      runImpl(stopwatches[0]);
    else
      runParallel();
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
  implements ITask, Comparable<Task>
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