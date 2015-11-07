package com.ThirtyNineEighty.Game.Factory;

import com.ThirtyNineEighty.Providers.*;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Game.Subprograms.*;
import com.ThirtyNineEighty.Providers.GLModelTankProvider;
import com.ThirtyNineEighty.Renderable.GL.*;
import com.ThirtyNineEighty.System.IRenderable;
import com.ThirtyNineEighty.System.ISubprogram;

import java.util.HashMap;

public class Factory
{
  private HashMap<String, Creator<? extends WorldObject<?, ?>>> objectCreators;
  private HashMap<String, Creator<? extends ISubprogram>> subprogramsCreators;
  private HashMap<String, Creator<? extends IDataProvider>> providersCreators;
  private HashMap<String, Creator<? extends IRenderable>> renderableCreators;

  public void initialize()
  {
    objectCreators = new HashMap<>();
    subprogramsCreators = new HashMap<>();
    providersCreators = new HashMap<>();
    renderableCreators = new HashMap<>();

    addObject("tank", Tank.class);
    addObject("botTank", Tank.class);
    addObject("land", Land.class);
    addObject("bullet", Bullet.class);
    addObject("building", Decor.class);
    addObject("aidKit", AidKit.class);

    addSubprogram("bot", BotSubprogram.class);
    addSubprogram("move", MoveSubprogram.class);
    addSubprogram("killMarkedCompletion", KillMarkedSubprogram.class);
    addSubprogram("rechargeSubprogram", RechargeSubprogram.class);

    addProvider("glModelTankProvider", GLModelTankProvider.class);
    addProvider("glModelLandProvider", GLModelLandProvider.class);
    addProvider("glModelWorldObjectProvider", GLModelWorldObjectProvider.class);

    addRenderable("glModel", GLModel.class);
  }

  public WorldObject<?, ?> createObject(String type, Object... params)
  {
    Creator<? extends WorldObject<?, ?>> creator = objectCreators.get(type);
    return creator.create(params);
  }

  public ISubprogram createSubprogram(String type, Object... params)
  {
    Creator<? extends ISubprogram> creator = subprogramsCreators.get(type);
    return creator.create(params);
  }

  public IDataProvider createProvider(String type, Object... params)
  {
    Creator<? extends IDataProvider> creator = providersCreators.get(type);
    return creator.create(params);
  }

  public IRenderable createRenderable(String type, Object... params)
  {
    Creator<? extends IRenderable> creator = renderableCreators.get(type);
    return creator.create(params);
  }

  private void addObject(String name, Class<? extends WorldObject<?, ?>> type)
  {
    objectCreators.put(name, new Creator<>(type));
  }

  private void addSubprogram(String name, Class<? extends ISubprogram> type)
  {
    subprogramsCreators.put(name, new Creator<>(type));
  }

  private void addProvider(String name, Class<? extends IDataProvider> type)
  {
    providersCreators.put(name, new Creator<>(type));
  }

  private void addRenderable(String name, Class<? extends IRenderable> type)
  {
    renderableCreators.put(name, new Creator<>(type));
  }
}
