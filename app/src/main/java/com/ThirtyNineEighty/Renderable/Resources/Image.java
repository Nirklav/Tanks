package com.ThirtyNineEighty.Renderable.Resources;

public class Image
{
  private Texture texture;
  private float[] coordinates;

  public Image(Texture tex, float[] coords)
  {
    texture = tex;
    coordinates = coords;
  }

  public Texture getTexture() { return texture; }
  public void setTexture(Texture value) { texture = value; }

  public float[] getCoordinates() { return coordinates; }
  public void setCoordinates(float[] value) { coordinates = value; }
}
