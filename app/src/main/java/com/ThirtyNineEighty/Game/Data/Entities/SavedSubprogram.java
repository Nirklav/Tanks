package com.ThirtyNineEighty.Game.Data.Entities;

import com.ThirtyNineEighty.Game.Factory.Creator;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.State;

import java.io.Serializable;

public class SavedSubprogram
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String type;
  public final State state;

  public SavedSubprogram(ISubprogram subprogram)
  {
    Class<?> subprogramClass = subprogram.getClass();
    type = subprogramClass.getName();
    state = subprogram.getState();
  }

  public ISubprogram load(WorldObject object)
  {
    try
    {
      Class<?> subprogramClass = Class.forName(type);
      Creator<?> playerCreator = new Creator<>(subprogramClass);
      return (ISubprogram)playerCreator.create(object, state);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
