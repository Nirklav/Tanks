package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;

import java.util.Collection;

public interface IMenu
{
  Collection<I2DRenderable> getControls();
  boolean processEvent(MotionEvent event);
}
