package com.ThirtyNineEighty.Game.Menu.Controls;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

public interface IControl extends I2DRenderable
{
  void processDown(int pointerId, float x, float y);
  void processMove(int pointerId, float x, float y);
  void processUp(int pointerId, float x, float y);
}
