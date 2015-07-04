package com.ThirtyNineEighty.Game.Menu;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.System.IBindable;

import java.util.List;

public interface IMenu
  extends IBindable
{
  // view support
  void fillRenderable(List<I2DRenderable> renderables);

  // touch support
  void processEvent(MotionEvent event);
}
