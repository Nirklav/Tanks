package com.ThirtyNineEighty.Base.Menus;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Base.IBindable;

public interface IMenu
  extends IBindable
{
  // touch support
  void processEvent(MotionEvent event);
}
