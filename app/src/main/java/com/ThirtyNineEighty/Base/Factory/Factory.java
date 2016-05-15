package com.ThirtyNineEighty.Base.Factory;

import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;
import com.ThirtyNineEighty.Base.Providers.*;
import com.ThirtyNineEighty.Base.Objects.*;
import com.ThirtyNineEighty.Base.Renderable.IRenderable;
import com.ThirtyNineEighty.Base.Subprograms.ISubprogram;
import com.ThirtyNineEighty.Base.Resources.Sources.FileDescriptionSource;

import java.util.HashMap;

public class Factory
{
  private final HashMap<String, Creator<? extends WorldObject<?, ?>>> objectCreators = new HashMap<>();
  private final HashMap<String, Creator<? extends ISubprogram>> subprogramsCreators = new HashMap<>();
  private final HashMap<String, Creator<? extends IDataProvider>> providersCreators = new HashMap<>();
  private final HashMap<String, Creator<? extends IRenderable>> renderableCreators = new HashMap<>();

  public WorldObject<?, ?> createObject(String descriptionStr)
  {
    Creator<? extends WorldObject<?, ?>> creator = getObjectCreator(descriptionStr);
    return creator.create(descriptionStr);
  }

  public WorldObject<?, ?> createObject(String descriptionStr, Properties properties)
  {
    Creator<? extends WorldObject<?, ?>> creator = getObjectCreator(descriptionStr);
    return creator.create(descriptionStr, properties);
  }

  private Creator<? extends WorldObject<?, ?>> getObjectCreator(String descriptionStr)
  {
    Description description = GameContext.resources.getDescription(new FileDescriptionSource(descriptionStr));
    return objectCreators.get(description.getObjectType());
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

  public void addObject(String name, Class<? extends WorldObject<?, ?>> type)
  {
    objectCreators.put(name, new Creator<>(type));
  }

  public void addSubprogram(String name, Class<? extends ISubprogram> type)
  {
    subprogramsCreators.put(name, new Creator<>(type));
  }

  public void addProvider(String name, Class<? extends IDataProvider> type)
  {
    providersCreators.put(name, new Creator<>(type));
  }

  public void addRenderable(String name, Class<? extends IRenderable> type)
  {
    renderableCreators.put(name, new Creator<>(type));
  }
}
