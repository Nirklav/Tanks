package com.ThirtyNineEighty.Game.Objects.Descriptions;

import java.io.Serializable;

public class VisualDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String modelName;
  public final String textureName;
  public final int index;

  public VisualDescription(String modelName, String textureName, int index)
  {
    this.modelName = modelName;
    this.textureName = textureName;
    this.index = index;
  }
}
