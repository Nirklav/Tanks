package com.ThirtyNineEighty.Game;

import java.util.ArrayList;

public class EngineObjectDescription
{
  public static class VisualModelDescription
  {
    public final String ModelName;
    public final String TextureName;

    public VisualModelDescription(String modelName, String textureName)
    {
      ModelName = modelName;
      TextureName = textureName;
    }
  }

  public static class PhysicalModelDescription
  {
    public final String ModelName;

    public PhysicalModelDescription(String modelName)
    {
      ModelName = modelName;
    }
  }

  public ArrayList<VisualModelDescription> VisualModels;
  public PhysicalModelDescription PhysicalModel;

  public EngineObjectDescription()
  {
    VisualModels = new ArrayList<VisualModelDescription>();
  }
}
