package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.System.IBindable;

public interface IControl
  extends IBindable
{
  void processDown(int pointerId, float x, float y);
  void processMove(int pointerId, float x, float y);
  void processUp(int pointerId, float x, float y);
}
