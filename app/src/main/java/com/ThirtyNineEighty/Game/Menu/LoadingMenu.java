package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;

public class LoadingMenu
  extends BaseMenu
{
  @Override
  public void initialize()
  {
    super.initialize();

    GLLabelProvider provider = new GLLabelProvider("Loading...");
    provider.setCharSize(45, 60);
    bind(new GLLabel(provider));
  }
}
