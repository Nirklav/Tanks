package com.ThirtyNineEighty.Base.Subprograms;

public interface ITaskAdder
{
  ITask schedule(int priority, Runnable task);
}
