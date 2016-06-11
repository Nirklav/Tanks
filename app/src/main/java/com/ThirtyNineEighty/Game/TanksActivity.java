package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Base.Collisions.CollisionManager;
import com.ThirtyNineEighty.Base.Content;
import com.ThirtyNineEighty.Base.Factory.Factory;
import com.ThirtyNineEighty.Base.GameActivity;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Objects.SkyBox;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.ContentState.ContentStateManager;
import com.ThirtyNineEighty.Game.ContentState.States.IContentState;
import com.ThirtyNineEighty.Game.ContentState.States.MainLoadingState;
import com.ThirtyNineEighty.Game.ContentState.States.MainState;
import com.ThirtyNineEighty.Game.Data.TanksDataBase;
import com.ThirtyNineEighty.Game.Data.TanksDataManager;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Providers.*;
import com.ThirtyNineEighty.Game.Resources.TanksResources;
import com.ThirtyNineEighty.Game.Subprograms.*;
import com.ThirtyNineEighty.Base.Renderable.GL.*;
import com.ThirtyNineEighty.Base.Providers.*;

public class TanksActivity
  extends GameActivity
{
  @Override
  protected void initializeContext()
  {
    super.initializeContext();

    GameContext.content = new Content();
    GameContext.collisions = new CollisionManager();
    GameContext.factory = new Factory();
    GameContext.resources = TanksContext.resources = new TanksResources();
    GameContext.data = TanksContext.data = new TanksDataManager(new TanksDataBase(this));
    TanksContext.contentState = new ContentStateManager();
  }

  @Override
  protected void initializeFactory()
  {
    super.initializeFactory();

    TanksContext.factory.addObject("skyBox", SkyBox.class);
    TanksContext.factory.addObject("tank", Tank.class);
    TanksContext.factory.addObject("bullet", Bullet.class);
    TanksContext.factory.addObject("decor", Decor.class);
    TanksContext.factory.addObject("aidKit", AidKit.class);
    TanksContext.factory.addObject("land", Land.class);

    TanksContext.factory.addSubprogram("bot", BotSubprogram.class);
    TanksContext.factory.addSubprogram("move", MoveSubprogram.class);
    TanksContext.factory.addSubprogram("killMarkedCompletion", KillMarkedSubprogram.class);
    TanksContext.factory.addSubprogram("rechargeSubprogram", RechargeSubprogram.class);
    TanksContext.factory.addSubprogram("respawnSubprogram", RespawnSubprogram.class);

    TanksContext.factory.addRenderable("glModel", GLModel.class);
    TanksContext.factory.addRenderable("glSkyBox", GLSkyBox.class);

    TanksContext.factory.addProvider("glModelTankProvider", GLModelTankProvider.class);
    TanksContext.factory.addProvider("glModelTurretProvider", GLModelTurretProvider.class);
    TanksContext.factory.addProvider("glModelLandProvider", GLModelLandProvider.class);
    TanksContext.factory.addProvider("glModelWorldObjectProvider", GLModelWorldObjectProvider.class);
    TanksContext.factory.addProvider("glRenderableSkyBoxProvider", GLRenderableSkyBoxProvider.class);
  }

  @Override
  protected void initializeContent()
  {
    super.initializeContent();

    TanksContext.content.postEvent(new Runnable()
    {
      @Override
      public void run()
      {
        IWorld saved = TanksContext.data.getSavedWorld(GameActivity.SavedWorld);
        IContentState state = saved != null
          ? new MainLoadingState(saved)
          : new MainState(false);

        TanksContext.contentState.set(state);
      }
    });
  }

  @Override
  protected void uninitialize()
  {
    super.uninitialize();

    GameContext.content = null;
    GameContext.collisions = null;
    GameContext.factory = null;
    TanksContext.contentState = null;

    GameContext.resources.clearCache();
    GameContext.resources = TanksContext.resources = null;

    GameContext.data.close();
    GameContext.data = TanksContext.data = null;
  }
}
