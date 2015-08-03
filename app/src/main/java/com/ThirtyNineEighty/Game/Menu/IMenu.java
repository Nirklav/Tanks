package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.System.IBindable;

public interface IMenu
  extends IBindable
{
  // touch support
  void processEvent(MotionEvent event);
}
