package com.ThirtyNineEighty.Game.Objects;

import java.io.Serializable;

public class EngineObjectDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static class VisualModel
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final String ModelName;
    public final String TextureName;

    public VisualModel(String modelName, String textureName)
    {
      ModelName = modelName;
      TextureName = textureName;
    }
  }

  public static class PhysicalModel
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final String ModelName;

    public PhysicalModel(String modelName)
    {
      ModelName = modelName;
    }
  }

  public VisualModel[] VisualModels;
  public PhysicalModel PhysicalModel;
}
