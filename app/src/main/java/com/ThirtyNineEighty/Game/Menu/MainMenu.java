package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Menu.Controls.Button;
import com.ThirtyNineEighty.Game.Worlds.GameStartArgs;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;

public class MainMenu
  extends BaseMenu
{
  @Override
  public void initialize()
  {
    super.initialize();

    Button continueButton = new Button("Continue");
    continueButton.setPosition(0, 250);
    continueButton.setSize(600, 200);
    continueButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        IWorld world = GameContext.content.getWorld();
        if (world == null)
          return;

        if (world instanceof GameWorld)
        {
          world.enable();
          GameContext.content.setMenu(new GameMenu());
        }
      }
    });
    addControl(continueButton);

    Button newGameButton = new Button("Solo");
    newGameButton.setPosition(0, 0);
    newGameButton.setSize(600, 200);
    newGameButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(null);
        GameContext.content.setMenu(new MapSelectMenu(new GameStartArgs()));
      }
    });
    addControl(newGameButton);

    Button campaign = new Button("Campaign");
    campaign.setPosition(0, -250);
    campaign.setSize(600, 200);
    campaign.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {

      }
    });
    addControl(campaign);
  }
}
