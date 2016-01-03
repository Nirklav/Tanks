package com.ThirtyNineEighty.Base.Objects.Descriptions;

import java.io.Serializable;

public class RenderableDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String modelName;
  public final String textureName;

  public RenderableDescription(String modelName, String textureName)
  {
    this.modelName = modelName;
    this.textureName = textureName;
  }
}
