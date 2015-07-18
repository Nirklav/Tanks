package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Game.Objects.AidKit;
import com.ThirtyNineEighty.Game.Objects.Bullet;
import com.ThirtyNineEighty.Game.Objects.Decor;
import com.ThirtyNineEighty.Game.Objects.EngineObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Land;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Subprograms.BotSubprogram;
import com.ThirtyNineEighty.Game.Subprograms.KillMarkedSubprogram;
import com.ThirtyNineEighty.Game.Subprograms.MoveSubprogram;
import com.ThirtyNineEighty.System.ISubprogram;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class MapFactory
{
  private HashMap<String, Class> objectBindings;
  private HashMap<String, Class> subprogramBindings;

  public MapFactory()
  {
    objectBindings = new HashMap<>();
    objectBindings.put("tank", Tank.class);
    objectBindings.put("botTank", Tank.class);
    objectBindings.put("land", Land.class);
    objectBindings.put("bullet", Bullet.class);
    objectBindings.put("building", Decor.class);
    objectBindings.put("aidKit", AidKit.class);

    subprogramBindings = new HashMap<>();
    subprogramBindings.put("bot", BotSubprogram.class);
    subprogramBindings.put("move", MoveSubprogram.class);
    subprogramBindings.put("killMarkedCompletion", KillMarkedSubprogram.class);
  }

  public EngineObject createObject(String type, String bulletType)
  {
    try
    {
      Class<?> objectClass = objectBindings.get(type);

      Constructor<?> stringStringCtor = getConstructor(objectClass, String.class, String.class);
      if (stringStringCtor != null)
        return (EngineObject) stringStringCtor.newInstance(type, bulletType);

      Constructor<?> stringCtor = getConstructor(objectClass, String.class);
      if (stringCtor != null)
        return (EngineObject) stringCtor.newInstance(type);

      Constructor<?> emptyCtor = getConstructor(objectClass);
      if (emptyCtor != null)
        return (EngineObject) emptyCtor.newInstance();

      throw new IllegalStateException("Object not contains constructor with string parameter");
    }
    catch (Exception e)
    {
      throw new RuntimeException(String.format("Can't create object with %s name", type), e);
    }
  }

  public ISubprogram createSubprogram(String type, Object parameter)
  {
    try
    {
      Class<?> subprogramClass = subprogramBindings.get(type);

      Constructor<?> gameObjectCtor = getConstructor(subprogramClass, GameObject.class);
      if (gameObjectCtor != null && parameter instanceof GameObject)
        return (ISubprogram) gameObjectCtor.newInstance(parameter);

      Constructor<?> engineObjectCtor = getConstructor(subprogramClass, EngineObject.class);
      if (engineObjectCtor != null && parameter instanceof EngineObject)
        return (ISubprogram) engineObjectCtor.newInstance(parameter);

      Constructor<?> emptyCtor = getConstructor(subprogramClass);
      if (emptyCtor != null && parameter == null)
        return (ISubprogram) emptyCtor.newInstance();

      throw new IllegalArgumentException("Object not contains right constructor with one of parameter: GameObject, EngineObject. Or it may be empty.");
    }
    catch (Exception e)
    {
      throw new RuntimeException(String.format("Can't create subprogram with %s name", type), e);
    }
  }

  private static <T> Constructor<T> getConstructor(Class<T> type, Class<?>... parametersTypes)
  {
    try
    {
      return type.getConstructor(parametersTypes);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }
}
