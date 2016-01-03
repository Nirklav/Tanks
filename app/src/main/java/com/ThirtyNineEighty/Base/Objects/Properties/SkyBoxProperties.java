package com.ThirtyNineEighty.Base.Objects.Properties;

public class SkyBoxProperties
  extends Properties
{
  private static final long serialVersionUID = 1L;

  private float scale;

  public SkyBoxProperties(float scale)
  {
    this.scale = scale;
  }

  public float getScale() { return scale; }
}
