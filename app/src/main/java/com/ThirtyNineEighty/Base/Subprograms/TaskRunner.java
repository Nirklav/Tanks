package com.ThirtyNineEighty.Base.Subprograms;

import android.support.annotation.NonNull;

import com.ThirtyNineEighty.Base.Common.Stopwatch;

import java.util.PriorityQueue;

public class TaskRunner
{
  private static final int MaxRunTime = 10;
  private static final int CriticalRunTime = 50;

  private final TaskAdder adder;
  private final PriorityQueue<Task> tasks;
  private final Stopwatch stopwatch;

  public TaskRunner()
  {
    adder = new TaskAdder();
    tasks = new PriorityQueue<>();
    stopwatch = new Stopwatch("TaskRunner", CriticalRunTime);
  }

  public void prepare(ISubprogram subprogram)
  {
    if (subprogram == null)
      throw new IllegalArgumentException("subprogram == null");

    subprogram.prepare(adder);
  }

  public void run()
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

  private class TaskAdder
    implements ITaskAdder
  {
    public ITask add(int priority, Runnable task)
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
