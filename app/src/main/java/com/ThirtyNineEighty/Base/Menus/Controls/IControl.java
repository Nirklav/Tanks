package com.ThirtyNineEighty.Base.Menus.Controls;

import com.ThirtyNineEighty.Base.IBindable;

public interface IControl
  extends IBindable
{
  void processDown(int pointerId, float x, float y);
  void processMove(int pointerId, float x, float y);
  void processUp(int pointerId, float x, float y);
}
