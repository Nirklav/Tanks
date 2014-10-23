package com.ThirtyNineEighty.Game.Menu;

/*
 * x and y always between 0.0f (left/top border) and 1.0f(right/bottom border)
 */
public interface IEventProcessor
{
  void processDown(int pointerId, float x, float y);
  void processMove(int pointerId, float x, float y);
  void processUp(int pointerId, float x, float y);
}
