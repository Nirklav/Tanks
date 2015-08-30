package com.ThirtyNineEighty.Game.Data.Entities;

import java.io.Serializable;
import java.util.List;

public class SavedGameEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public String mapName;
  public List<SavedSubprogram> worldSubprograms;
  public SavedObject player;
  public List<SavedObject> objects;
}
