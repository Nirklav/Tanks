package com.ThirtyNineEighty.Game.Objects.Descriptions;

import java.io.Serializable;

public class VisualDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String modelName;
  public final String textureName;
  public final int id;

  public VisualDescription(String modelName, String textureName, int id)
  {
    this.modelName = modelName;
    this.textureName = textureName;
    this.id = id;
  }
}
