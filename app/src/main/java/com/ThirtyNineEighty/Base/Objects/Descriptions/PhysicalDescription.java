package com.ThirtyNineEighty.Base.Objects.Descriptions;

import java.io.Serializable;

public class PhysicalDescription
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public final String modelName;

  public PhysicalDescription(String modelName)
  {
    this.modelName = modelName;
  }
}
