package com.ThirtyNineEighty.Game.Menu;

public interface IEventProcessor
{
  void processDown(int pointerId, float x, float y);
  void processMove(int pointerId, float x, float y);
  void processUp(int pointerId, float x, float y);
}
