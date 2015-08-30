package com.ThirtyNineEighty.Game.Data.Entities;

import com.ThirtyNineEighty.Game.Map.Factory.Creator;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SavedObject
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String type;
  public final State object;
  public final List<SavedSubprogram> subprograms;

  public SavedObject(WorldObject worldObject)
  {
    Class<?> objectClass = worldObject.getClass();
    type = objectClass.getName();
    object = worldObject.getState();
    subprograms = new ArrayList<>();

    for (ISubprogram subprogram : worldObject.getSubprograms())
      subprograms.add(new SavedSubprogram(subprogram));
  }

  public WorldObject load()
  {
    try
    {
      Class<?> objectClass = Class.forName(type);
      Creator<?> playerCreator = new Creator<>(objectClass);
      WorldObject worldObject = (WorldObject)playerCreator.create(object);

      for (SavedSubprogram saved : subprograms)
      {
        ISubprogram subprogram = saved.load(worldObject);
        worldObject.bind(subprogram);
      }

      return worldObject;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
