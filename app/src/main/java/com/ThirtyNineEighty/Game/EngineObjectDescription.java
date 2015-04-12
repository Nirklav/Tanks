package com.ThirtyNineEighty.Game;

import java.io.Serializable;

public class EngineObjectDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static class VisualModelDescription
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final String ModelName;
    public final String TextureName;

    public VisualModelDescription(String modelName, String textureName)
    {
      ModelName = modelName;
      TextureName = textureName;
    }
  }

  public static class PhysicalModelDescription
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public final String ModelName;

    public PhysicalModelDescription(String modelName)
    {
      ModelName = modelName;
    }
  }

  public VisualModelDescription[] VisualModels;
  public PhysicalModelDescription PhysicalModel;
}
