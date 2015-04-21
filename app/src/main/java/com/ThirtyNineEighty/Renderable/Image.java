package com.ThirtyNineEighty.Renderable;

public class Image
{
  public final Texture texture;
  public final float[] coordinates;

  public Image(Texture tex, float[] coords)
  {
    texture = tex;
    coordinates = coords;
  }
}
