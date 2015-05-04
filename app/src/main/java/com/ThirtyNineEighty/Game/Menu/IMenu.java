package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

import java.util.List;

public interface IMenu
{
  // life cycle
  boolean isInitialized();

  void initialize(Object args);
  void uninitialize();

  // view support
  void fillRenderable(List<I2DRenderable> renderables);

  // touch support
  void processEvent(MotionEvent event);
}
