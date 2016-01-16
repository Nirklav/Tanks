package com.ThirtyNineEighty.Base.Resources.Entities;

public class Image
  extends Resource
{
  private static final long serialVersionUID = 1L;

  private String textureName;
  private float[] coordinates;

  public Image(String name, String textureName, float[] coordinates)
  {
    super(name);

    this.textureName = textureName;
    this.coordinates = coordinates;
  }

  public String getTextureName() { return textureName; }
  public float[] getCoordinates() { return coordinates; }
}
