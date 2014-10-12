package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.I2DRenderable;

import java.util.Collection;

public interface IMenu
{
  Collection<I2DRenderable> getControls();
  boolean processEvent(MotionEvent event, float width, float height);
}
