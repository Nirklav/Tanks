package com.ThirtyNineEighty.System;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Game.Worlds.IWorld;

public interface IContent
{
  void setWorld(IWorld world);
  void setWorld(IWorld world, Object args);
  IWorld getWorld();

  void setMenu(IMenu menu);
  void setMenu(IMenu menu, Object args);
  IMenu getMenu();

  void bindProgram(ISubprogram subprogram);
  void unbindProgram(ISubprogram subprogram);
}
