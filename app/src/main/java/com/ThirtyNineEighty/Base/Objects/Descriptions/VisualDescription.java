package com.ThirtyNineEighty.Base.Objects.Descriptions;

import java.io.Serializable;

public class VisualDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final int id;
  public final String type;
  public final String providerType;

  public final RenderableDescription renderable;

  public VisualDescription(int id, String type, RenderableDescription renderable, String providerType)
  {
    this.type = type;
    this.renderable = renderable;
    this.providerType = providerType;
    this.id = id;
  }
}
