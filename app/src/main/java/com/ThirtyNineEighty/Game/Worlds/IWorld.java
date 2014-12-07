package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.Content;

import java.util.List;

public interface IWorld
{
  void initialize(Content content, Object args);
  void update();

  void setViewMatrix(float[] viewMatrix);
  void fillRenderable(List<I3DRenderable> renderables);

  IMenu getMenu();
}
