package com.ThirtyNineEighty.Game.Objects.Descriptions;

import java.io.Serializable;

public class VisualDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String type;
  public final String providerType;

  public final RenderableDescription renderable;

  public final int id;

  public VisualDescription(String type, RenderableDescription renderable, String providerType, int id)
  {
    this.type = type;
    this.renderable = renderable;
    this.providerType = providerType;
    this.id = id;
  }
}
