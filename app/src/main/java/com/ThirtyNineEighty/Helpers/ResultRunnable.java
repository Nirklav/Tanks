package com.ThirtyNineEighty.Helpers;

public abstract class ResultRunnable<TResult>
  implements Runnable
{
  private TResult result;
  public TResult getResult() { return result; }

  @Override
  public void run()
  {
    result = onRun();
  }

  protected abstract TResult onRun();
}
